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
  上面三个步骤前文已介绍过。



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

![]()




















