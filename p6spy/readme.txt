1.1.1只需要修改这里就可以
spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver
3.7.0需要修改这里就可以driver-class-name和url
<!-- 打印sql -->
<dependency>
	<groupId>p6spy</groupId>
	<artifactId>p6spy</artifactId>
	<version>3.7.0</version>
</dependency>
spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver
spring.datasource.url=jdbc:p6spy:mysql://192.168.9.250:3306/process?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&useSSL=false