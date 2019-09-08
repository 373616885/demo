# **spring** 源码日志

**JAVA类加载流程**

-  **Bootstrap ClassLoader**  （是由C++编写的-- 虚拟机的一部分）最顶层的加载类，主要加载核心类库，%JRE_HOME%\lib下的rt.jar、resources.jar、charsets.jar和class等
- **ExtClassLoader** 扩展的类加载器，加载目录%JRE_HOME%\lib\ext目录下的jar包和class文件
- **AppClassLoader**  **也称为SystemAppClass** 加载当前应用的classpath的所有类

```java
ClassLoader c = Test.class.getClassLoader();  //获取Test类的类加载器
System.out.println(c); 
ClassLoader c1 = c.getParent();  //获取c这个类加载器的父类加载器
System.out.println(c1);
ClassLoader c2 = c1.getParent();//获取c1这个类加载器的父类加载器
System.out.println(c2); // Bootstrap ClassLoader 不是java的写所以为 null
System.out.println(ClassUtils.getDefaultClassLoader());//spring 加载的
// BootstrapClassLoader 加载目录
String[] files = System.getProperty("sun.boot.class.path").split(";");
Arrays.asList(files).forEach(System.out::println);
// ExtClassLoader  加载目录
String[] extFiles = System.getProperty("java.ext.dirs").split(";");
Arrays.asList(extFiles).forEach(System.out::println); 
// AppClassLoader  加载目录
String[] appFiles = System.getProperty("java.class.path").split(";");
Arrays.asList(appFiles).forEach(System.out::println); 
```

**ClassLoader双亲委派加载源码**

1. 当AppClassLoader加载一个class时，它首先不会自己去尝试加载这个类，而是把类加载请求委派给父类加载器ExtClassLoader去完成。

2. 当ExtClassLoader加载一个class时，它首先也不会自己去尝试加载这个类，而是把类加载请求委派给BootStrapClassLoader去完成。

3. 如果BootStrapClassLoader加载失败（例如在$JAVA_HOME/jre/lib里未查找到该class），会使用ExtClassLoader来尝试加载。

4. 若ExtClassLoader也加载失败，则会使用AppClassLoader来加载，如果AppClassLoader也加载失败，则会报出异常ClassNotFoundException。

**ClassLoader类的loadClass方法：** 

​	委托是从下向上（app--ext--boot）具体查找过程却是自上至下 findClass()

   ```java
   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
       // First, check if the class has already been loaded
       // 首先找缓存（findLoadedClass）
       Class c = findLoadedClass(name);
       if (c == null) {
           try {
               if (parent != null) {
                   // 用parent来递归的loadClass -- (AppClassLoader)
                   c = parent.loadClass(name, false);
               } else {
                   // ExtClassLoader并没有设置parent 
                   // 使用BootStrapClassLoader来加载class
                   c = findBootstrapClassOrNull(name);
               }
           } catch (ClassNotFoundException e) {
               // ClassNotFoundException thrown if class not found
               // from the non-null parent class loader
           }
           if (c == null) {
               // If still not found, then invoke findClass in order
               // to find the class.
               // 这个是先从 BootStrapClassLoader查找
               // 然后到 ExtClassLoade
               // 最后到 AppClassLoader
               c = findClass(name);
           }
       }
       if (resolve) {
           resolveClass(c);
       }
       return c;
   }
   ```

**为什么使用双亲委派机制：**

 	双亲委派机制是为了安全而设计的，比如我可以自定义一个java.lang.Integer类来覆盖jdk中默认的Integer类，破坏应用程序的正常进行。使用双亲委派机制的话该Integer类永远不会被调用，以为委托BootStrapClassLoader加载后会加载JDK中的Integer类而不会加载自定义的Integer类

**contextClassLoader**：

​	每一个线程都有一个相关联的ClassLoader，默认是AppClassLoader

​	主要解决一些类似于spi的服务，常见的就是jdbc 。jdbc接口在%JRE_HOME%\lib下的rt.jar，而实现类在 classpath下面 。

​	在rt.jar里面的DriverManager想使用具体的实现类 ，但它是有BootStrapClassLoader加载的，而具体的实现类是由AppClassLoader加载的 ，根据隔离原则，DriverManager中是访问不到mysql具体方法的。

​	解决使用contextClassLoader 去获取 AppClassLoader 就可以直接访问mysql类了

​	这其实打破了类的委托机制，这种机制普遍存在于好多第三方的工具中，如tomcat、spring中

**DriverManager类的getConnection方法**：

![](img/20170922183420138.png)

​	



----------------------------------------------------------------------------------------------------------------------------------------------------------------

加载文件时使用以下代码： 

```java
Resource resource= new ClassPathResource("beanFactoryTest.xml");
InputStream inputStream = resource.getInputStream();
```

 profile 属性的使用：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
	<beans prfile="dev">
	</beans> 
    <beans prile="pro">
	</beans> 
    <bean id="myTestBean" name ="one,testBean,testBean2" class="com.qin.start.bean.MyTestBean" >
        <meta key="1" value="2"/>
    </bean>
</beans>
```

```xml
<context-param> 
    <param-name>spring.profile.active</param-name> 
    <param-value>dev</param-value> 
</context-param> 
```



















