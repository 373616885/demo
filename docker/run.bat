#mvn clean install -Dmaven.test.skip=true
mvn clean package dockerfile:build -Dmaven.test.skip=true