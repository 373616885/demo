### autowire

autowire 即自动注入的意思，通过使用 autowire 特性，我们就不用再显示的配置 bean 之间的依赖了

当 bean 配置中的 autowire = byName 时，Spring 会首先通过反射获取该 bean 所依赖 bean 的名字（beanName），然后再通过调用 BeanFactory.getName(beanName) 方法即可获取对应的依赖实例。

autowire = byName 原理大致就是这样，接下来我们来演示一下

```java
public class Service {

    private Dao mysqlDao;

    private Dao mongoDao;

    // 忽略 getter/setter

    @Override
    public String toString() {
        return super.toString() + "\n\t\t\t\t\t{" +
            "mysqlDao=" + mysqlDao +
            ", mongoDao=" + mongoDao +
            '}';
    }
}

public interface Dao {}
public class MySqlDao implements Dao {}
public class MongoDao implements Dao {}

```

```xml
<bean name="mongoDao" class="xyz.coolblog.autowire.MongoDao"/>
<bean name="mysqlDao" class="xyz.coolblog.autowire.MySqlDao"/>

<!-- 非自动注入，手动配置依赖 -->
<bean name="service-without-autowire" class="xyz.coolblog.autowire.Service" autowire="no">
    <property name="mysqlDao" ref="mysqlDao"/>
    <property name="mongoDao" ref="mongoDao"/>
</bean>

<!-- 通过设置 autowire 属性，我们就不需要像上面那样显式配置依赖了 -->
<bean name="service-with-autowire" class="xyz.coolblog.autowire.Service" autowire="byName"/>
```

```java
String configLocation = "application-autowire.xml";

ApplicationContext applicationContext = 
    new ClassPathXmlApplicationContext(configLocation);
System.out.println("service-without-autowire -> " + applicationContext.getBean("service-without-autowire"));

System.out.println("service-with-autowire -> " + applicationContext.getBean("service-with-autowire"));

```

