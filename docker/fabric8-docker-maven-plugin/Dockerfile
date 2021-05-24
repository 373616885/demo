# 指定基础镜像，必须为第一个命令
FROM openjdk:11
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
EXPOSE 8080

# 工作目录，类似于cd命令
WORKDIR /home/apply/docker

# 查看java版本
CMD ["java", "-version"]

# 执行启动命令
ENTRYPOINT ["java","-jar","/home/apply/docker/docker.jar"]