原理：

docker inspect  docker-qin-0.0.2

```shell
 将class，resources，libs放到对应得文件中
 /app/resources:
 /app/classes:
 /app/libs/
 然后：
 java -cp /app/resources:/app/classes:/app/libs/* com.qin.docker.DockerApplication
 
 "Entrypoint": [
                "java",
                "-Xms512m",
                "-Xmx512m",
                "-Duser.timezone=GMT+08",
                "-Dfile.encoding=UTF8",
                "-cp",
                "/app/resources:/app/classes:/app/libs/*",
                "com.qin.docker.DockerApplication"
   ],
```

