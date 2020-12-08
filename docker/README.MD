**dockerfile-maven-plugin遇到巨坑，1.4.10版本windows10不能在pom.xml中使用**

**必须把Dockerfile放到项目根目录下**



#### `docker-maven-plugin` 插件

```xml
<build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.4.13</version>
                <executions>
                    <execution>
                        <id>default</id>
                        <goals>
                            <!--构建镜像-->
                            <goal>build</goal>
                            <!--推送私有仓库 （ 不想推送就注释掉这个 ）-->
                            <goal>push</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!--<skip>true</skip>-->
                    <!-- 镜像名称 -->
                    <repository>qin/${project.name}</repository>
                    <!-- 镜像标签 -->
                    <tag>${project.version}</tag>
                    <buildArgs>
                        <!-- 向Dockerfile传递的参数 -->
                        <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                    <dockerfile>Dockerfile</dockerfile>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

#### 插件地址

> https://github.com/spotify/dockerfile-maven



这个Maven 插件配合Dockerfile 文件构建 Docker 镜像

将 `mvn dockerfile:build` 和 `mvn dockerfile:push` 整合到 `mvn package`

直接使用 `mvn package` 就可以同时打包和构建 Docker 镜像



#### 前置条件

- 服务器上Docker安装成功
- Docker开启远程访问
- 客户机配置好DOCKER_HOST环境变量



#### 开启Docker的远程端口2375

```shell
# vim编辑docker配置文件/lib/systemd/system/docker.service，并修改ExecStart为下面的内容
# vim /lib/systemd/system/docker.service
#ExecStart=后面加上 -H tcp://0.0.0.0:2375
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock -H tcp://0.0.0.0:2375

# 1，加载docker守护线程
systemctl daemon-reload
# 2，重启docker 
systemctl restart docker

```

#### Docker开启远程访问

```shell
# 测试2375端口是否开启成功
telnet 192.168.11.88 2375
```

#### 客户机配置好DOCKER_HOST环境变量

电脑配置环境变量

```shell
DOCKER_HOST  tcp://192.168.11.88:2375

echo %DOCKER_HOST%
```



#### Dockerfile 文件

```dockerfile
# 指定基础镜像，必须为第一个命令
FROM openjdk:8-alpine
#FROM java:8

# 维护者信息
MAINTAINER qinjp 373616885@qq.com

#RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
# 构建镜像时执行的命令
RUN echo Asia/Shanghai > /etc/timezone

# 添加参数 -- 从 Docker Maven 插件中指定的构建参数
ARG JAR_FILE

# 将target目录下的JAR_FILE 添加 容器中的docker.jar
ADD target/${JAR_FILE} /home/apply/docker/docker.jar

# 存在就修改创建时间
RUN touch /home/apply/docker/docker.jar

# 用于指定持久化目录（容器内）--这个目录持久化--不会重启就没了 -- docker inspect 容积 查看具体的挂载目录
VOLUME ["/home/apply/docker/logs"]

# 指定于外界交互的端口
EXPOSE 80

# 工作目录，类似于cd命令
WORKDIR /home/apply/docker

# 查看java版本
CMD ["java", "-version"]

# 执行启动命令
ENTRYPOINT ["java","-jar","/home/apply/docker/docker.jar"]
```



####  IDEA 操作 Docker

IDEA 菜单栏里找到 View -> Tool windows -> Services  打开 Services 面板

然后点击 Add service > Docker Connection 来添加一个 Docker 连接

填上

name: docker

TCP socket -> Engine API URL : tcp://47.100.185.77:2375

在 images 里创建容器

填上

Container name : docker

Run options: -p 8080:80



编译项目并构建docker镜像，编译结束自动推送镜像到docker主机中

`mvn clean package -Dmaven.test.skip=true dockerfile:build -Ddockerfile.tag=0.0.1`





#### Docker设置TLS 保证端口2375的安全连接

`sh create_tls_certs.sh`

```powershell
#创建 Docker TLS 证书
#!/bin/bash

#相关配置信息 -- SERVER 外网ip 必须设置不然
#idea 会出现 docker chanel disconnected before any data was received
SERVER="47.100.185.77"
PASSWORD="373616885"
COUNTRY="CN"
STATE="广东省"
CITY="深圳市"
ORGANIZATION="公司名称"
ORGANIZATIONAL_UNIT="公司单位"
EMAIL="373616885@qq.com"

###开始生成文件###
echo "开始生成文件"

#切换到生产密钥的目录
cd /usr/local/docker/ssl
#生成ca私钥(使用aes256加密)
openssl genrsa -aes256 -passout pass:$PASSWORD  -out ca-key.pem 2048
#生成ca证书，填写配置信息
openssl req -new -x509 -passin "pass:$PASSWORD" -days 3650 -key ca-key.pem -sha256 -out ca.pem -subj "/C=$COUNTRY/ST=$STATE/L=$CITY/O=$ORGANIZATION/OU=$ORGANIZATIONAL_UNIT/CN=$SERVER/emailAddress=$EMAIL"

#生成server证书私钥文件
openssl genrsa -out server-key.pem 2048
#生成server证书请求文件
openssl req -subj "/CN=$SERVER" -new -key server-key.pem -out server.csr
# 0.0.0.0可以匹配任意，但是仍然需要配置你的服务器外网ip
# 省略会造成错误 docker chanel disconnected before any data was received
sh -c  'echo "subjectAltName = IP:'$SERVER',IP:0.0.0.0" >> extfile-server.cnf'
sh -c  'echo "extendedKeyUsage = serverAuth" >> extfile-server.cnf'
#使用CA证书及CA密钥以及上面的server证书请求文件进行签发，生成server自签证书
openssl x509 -req -days 3650 -in server.csr -CA ca.pem -CAkey ca-key.pem -passin "pass:$PASSWORD" -CAcreateserial  -out server-cert.pem -extfile extfile-server.cnf

#生成client证书RSA私钥文件
openssl genrsa -out key.pem 2048
#生成client证书请求文件
openssl req -subj '/CN=client' -new -key key.pem -out client.csr

sh -c 'echo "extendedKeyUsage=clientAuth" > extfile-client.cnf'
#生成client自签证书（根据上面的client私钥文件、client证书请求文件生成）
openssl x509 -req -days 3650 -in client.csr -CA ca.pem -CAkey ca-key.pem  -passin "pass:$PASSWORD" -CAcreateserial -out cert.pem  -extfile extfile-client.cnf

#更改密钥权限
chmod 0400 ca-key.pem key.pem server-key.pem
#更改密钥权限
chmod 0444 ca.pem server-cert.pem cert.pem
#删除无用文件
rm client.csr server.csr

echo "生成文件完成"
###生成结束###


```



`vim /usr/lib/systemd/system/docker.service`

```shell
# ExecStart 加上 --tlscacert=/usr/local/docker/ssl/ca.pem --tlscert=/usr/local/docker/ssl/server-cert.pem --tlskey=/usr/local/docker/ssl/server-key.pem

# 示例
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock -H tcp://0.0.0.0:2375 --tlsverify --tlscacert=/usr/local/docker/ssl/ca.pem --tlscert=/usr/local/docker/ssl/server-cert.pem --tlskey=/usr/local/docker/ssl/server-key.pem

# 重启
systemctl daemon-reload
systemctl restart docker

# 检查--出现错误就证明成功
docker -H tcp://47.100.185.77:2375 ps -a
```



windows下配置环境变量：

```shell
DOCKER_HOST=tcp://47.100.185.77:2376
DOCKER_CERT_PATH=E:\docker\ssl
DOCKER_TLS_VERIFY=1

证书路径目录下面有三个证书文件 ca.pem、cert.pem、key.pem
```



`mvn clean package -Dmaven.test.skip=true`



idea 

```shell
Engine API URL : tcp://47.100.185.77:2375
改成：
Engine API URL : https://47.100.185.77:2375
Certificates folder : E:\docker\ssl

```





