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

​	双亲委派机制是为了安全而设计的，比如我可以自定义一个java.lang.Integer类来覆盖jdk中默认的Integer类，破坏应用程序的正常进行。使用双亲委派机制的话该Integer类永远不会被调用，以为委托BootStrapClassLoader加载后会加载JDK中的Integer类而不会加载自定义的Integer类 	

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

**加载文件时使用以下代码：** 

```java
Resource resource= new ClassPathResource("beanFactoryTest.xml");
InputStream inputStream = resource.getInputStream();
```

 **profile 属性的使用：**

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

**manualSingletonNames 存储手动注册的bean :**

```java
xmlBeanFactory.registerSingleton("myDog", new Dog());
```



### AbstractBeanFactory.doGetBean()   方法

```java
protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
                              @Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {
        // 转换对应 beanName 
		// 传人的参数 name 参数除了正常的面层，也有可能是别名和 FactoryBean
        final String beanName = transformedBeanName(name);
        Object bean;
		
        // Eagerly check singleton cache for manually registered singletons.
        // 尝试从缓存中加载单例    
    	// 首先尝试从缓存中加载，如果加载不成功则再次尝试从 singletonFactories 中加载
    	// 因为在创建单例 bean 的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，
    	// 在 Spring 中创建 bean 的原则是不等 bean 创建完成就会将创建 bean 的 ObjectFactory 
        // 提早曝光加入到缓存中，一旦下一个 bean 创建时候需要依赖上一个 bean 则直接使用ObjectFactory
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null && args == null) {
            if (logger.isDebugEnabled()) {
                if (isSingletonCurrentlyInCreation(beanName)) {
                    logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                            "' that is not fully initialized yet - a consequence of a circular reference");
                }
                else {
                    logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
                }
            }
      		// 此处获取的可能是bean,
            // 也可能是FactoryBean类型的getObject方法的返回值（ObjectFactory），
            // FactoryBean#getObject（）方法所返回的对
            // 需要进行处理，得到bean
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }

        else {
            // Fail if we're already creating this bean instance:
            // We're assumably within a circular reference.
            // 当前线程如果存在正在创建的bean 则证明这里存在循环依赖的问题
            // 如果存在 A 中有 B 的属性， B 中有 A 的属性， 
            // 那么当依赖注入的时候，就会产生当 A 还未创建完的时候因为对于 B 的创建再次返回创建 A,
            // 造成循环依赖，也就是情况： isPrototypeCurrentlyInCreation(beanName）判断 true
            if (isPrototypeCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }

            // Check if bean definition exists in this factory.
            // 父类工厂
            BeanFactory parentBeanFactory = getParentBeanFactory();
            // 存在父类工厂，且当前工厂类不包含BeanDefintion信息
            if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
                // Not found -> check parent.
                // 获取原始bean的名称
                String nameToLookup = originalBeanName(name);
                // 递归到BeanFactory查找
                if (parentBeanFactory instanceof AbstractBeanFactory) {
                    return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
                            nameToLookup, requiredType, args, typeCheckOnly);
                }
                else if (args != null) {
                    // Delegation to parent with explicit args.
                    return (T) parentBeanFactory.getBean(nameToLookup, args);
                }
                else {
                    // No args -> delegate to standard getBean method.
                    return parentBeanFactory.getBean(nameToLookup, requiredType);
                }
            }

            if (!typeCheckOnly) {
                // 标记为正在创建
                markBeanAsCreated(beanName);
            }

            try {
                // 将存储 XML 配置文件的 GernericBeanDefinition 转换为 RootBeanDefinition 
                // 合并相关的的父类属性（如果存在）
                // 因为后续所有的bean都是针对于 RootBeanDefinition 处理 
                final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                // 检查 RootBeanDefinition 是不是抽象类
                checkMergedBeanDefinition(mbd, beanName, args);

                // Guarantee initialization of beans that the current bean depends on.
                // dependsOn 依赖关系先解决
                String[] dependsOn = mbd.getDependsOn();
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        if (isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }
                        // 标记依赖的注册关系
                        // dependentBeanMap 存放 dep - beanName
                        // dependenciesForBeanMap  存放 beanName - dep
                        registerDependentBean(dep, beanName);
                        try {
                            // 递归 getBean 获取实例
                            getBean(dep);
                        }
                        catch (NoSuchBeanDefinitionException ex) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                        }
                    }
                }

                // Create bean instance.
                // 针对scope= singleton（默认）创建bean
                if (mbd.isSingleton()) {
                    sharedInstance = getSingleton(beanName, () -> {
                        try {
                            Object result = createBean(beanName, mbd, args);
                            return result;
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            destroySingleton(beanName);
                            throw ex;
                        }
                    });
                    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                }
				// 针对scope= prototype 创建bean
                else if (mbd.isPrototype()) {
                    // It's a prototype -> create a new instance.
                    Object prototypeInstance = null;
                    try {
                        beforePrototypeCreation(beanName);
                        prototypeInstance = createBean(beanName, mbd, args);
                    }
                    finally {
                        afterPrototypeCreation(beanName);
                    }
                    bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                }

                else {
                    // 针对scope= 其他 （request之类的） 创建bean
                    String scopeName = mbd.getScope();
                    final Scope scope = this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }
                    try {
                        Object scopedInstance = scope.get(beanName, () -> {
                            beforePrototypeCreation(beanName);
                            try {
                                return createBean(beanName, mbd, args);
                            }
                            finally {
                                afterPrototypeCreation(beanName);
                            }
                        });
                        bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    }
                    catch (IllegalStateException ex) {
                        throw new BeanCreationException(beanName,
                                "Scope '" + scopeName + "' is not active for the current thread; consider " +
                                        "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                                ex);
                    }
                }
            }
            catch (BeansException ex) {
                cleanupAfterBeanCreationFailure(beanName);
                throw ex;
            }
        }

        // Check if required type matches the type of the actual bean instance.
    	// 类型转换 
    	// 可能会存在这样的情况，返回的 bean 其实是个 String，
    	// 但是 requiredType 却传人 Integer 类型，那么这时候本步骤就会起作用了
        if (requiredType != null && !requiredType.isInstance(bean)) {
            try {
                T convertedBean = getTypeConverter().convertIfNecessary(bean, requiredType);
                if (convertedBean == null) {
                    throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
                }
                return convertedBean;
            }
            catch (TypeMismatchException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to convert bean '" + name + "' to required type '" +
                            ClassUtils.getQualifiedName(requiredType) + "'", ex);
                }
                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
        }
        return (T) bean;
    }
```

总体步骤：

1. 转换对应 beanName 
2. 尝试从缓存中加载单例
3. bean 的实例化
4. 原型模式的依赖检查 （isPrototypeCurrentlyInCreation）
5. 检测 parentBeanFactory
6. 将存储 XML 配置文件的 GernericBeanDefinition 转换为 RootBeanDefinition
7. 寻找依赖（dependsOn）
8. 针对不同的 scope 进行 bean 的创建
9. 类型转换

### 缓存中获取单例 bean

```java
public Object getSingleton(String beanName) {
    // 参数 true 设置标识允许早期依赖 
	return getSingleton(beanName, true);
}
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
   		// 检查缓存中是否存在--之前已经创建过了
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            // 如果为空，则锁定全局变盎并进行处理
			synchronized (this.singletonObjects) {
                // 再从 earlySingletonObjects 里面获取
				singletonObject = this.earlySingletonObjects.get(beanName);
				if (singletonObject == null && allowEarlyReference) {
                    // 尝试从 singletonFactories 里面获取 beanName 对应的 ObjectFactory
                    // 某些方法初始化前，会调用： addSingletonFactory方法将对应的
                    // 将ObjectFactory存储在singletonFactories中
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
                        // 调用预先的getObject方法
						singletonObject = singletonFactory.getObject();
                        // 记录缓存中--earlySingletonObjects和singletonFactories互斥的
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return singletonObject;
	}


```



- singletonObjects：用于保存 BeanName 和创建 bean 实例之间的关系， bean name -> beaninstance
-  singletonFactories ：用于保存 BeanName 和创建 bean 的工厂之间的关系，bean name -> ObjectFactory
- earlySingletonObjects ：也是保存 BeanName 和创建 bean 实例之间的关系，与 singletonObjects 的不同之处在于，当一个单例 bean 被放到这里面后，那么当 bean 还在创建过程中，就可以通过 getBean 方法获取到了，其目的是用来检测循环引用 。( earlySingletonObjects 与 singletonFactories  互斥 )
-  registeredSingletons：用来保存当前所有巳注册的 bean

### 简单的 bean 的生命周期

```java
// 刚开始的getBean() - doGetBean() - 单例模式的创建bean 
1. AbstractBeanFactory.doGetBean()
if (mbd.isSingleton()) {
    sharedInstance = getSingleton(beanName, () -> {
        try {
            return createBean(beanName, mbd, args);
        }
        catch (BeansException ex) {
       // Explicitly remove instance from singleton cache: It might have been put there
	   // eagerly by the creation process, to allow for circular reference resolution.
	   // Also remove any beans that received a temporary reference to the bean.
            destroySingleton(beanName);
            throw ex;
        }
    });
    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
}

//DefaultSingletonBeanRegistry.getSingleton() -> 上面的 createBean(beanName, mbd, args);  
2.AbstractAutowireCapableBeanFactory.createBean () 


    
//Give BeanPostProcessors a chance to return a proxy instead of the target bean instance 
//这里改变原始的bean变成代理的bean  
    
//实现BeanPostProcessor的实现类注册到IOC中
//所有的bean都经过bean的后置处理器BeanPostProcessor 
//分别在bean的初始化前后对bean对象提供自己的实例化逻辑
//postProcessAfterInitialization：初始化之后对bean进行增强处理
//postProcessBeforeInitialization：初始化之前对bean进行增强处理
    
//AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsBeforeInstantiation 
//InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation   
    
//AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization
//BeanPostProcessors.postProcessAfterInitialization  
3.Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
    bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
    if (bean != null) {
        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
    }
	
    
    
    
```

