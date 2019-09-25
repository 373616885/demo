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
    	// 和originalBeanName(name);区别是这里获取到的可能是别名
        final String beanName = transformedBeanName(name);
        Object bean;
		
        // Eagerly check singleton cache for manually registered singletons.
        // 尝试从缓存中 singletonObjects 加载单例    
    	// 如果加载不成功则再次尝试从 earlySingletonObjects 中加载 （循环依赖中的提前加载）
    	// 最后尝试从 singletonFactories 中加载获取提前曝光的 ObjectFactory  
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
            // 也可能是FactoryBean (实现了FactoryBean的bean)
            // 需要对FactoryBean#getObject（）方法所返回的进行处理，得到bean
            // 当这里是有FactoryBean#getObject获取bean的时候会
            // 会调用beanPostProcess的后置处理方法
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }

        else {
            // Fail if we're already creating this bean instance:
            // We're assumably within a circular reference.
            // 这里只是证明非单例的循环依赖问题
            // 当前线程如果存在正在创建的bean 则证明这里存在循环依赖的问题
            // 如果存在 A 中有 B 的属性， B 中有 A 的属性， 
            // 那么当依赖注入的时候，就会产生当 A 还未创建完的时候因为对于 B 的创建再次返回创建 A,
            // 造成循环依赖，也就是情况： isPrototypeCurrentlyInCreation(beanName）判断 true
            // 通过prototypesCurrentlyInCreation 这个属性判断
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
                // 标记为正在创建 -- alreadyCreated 这个属性标记这个name已经创建了
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
                        // 判断循环依赖的问题
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
            // 如果为空，则锁定全局变量并进行处理
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
                        // 实际调用getEarlyBeanReference(beanName, mbd, bean)方法
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



### 创建bean的过程

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
    // 解析Class-- ClassUtils.getDefaultClassLoader()
    // Thread.currentThread().getContextClassLoader()
    // AppClassLoader
	Class<?> resolvedClass = resolveBeanClass(mbd, beanName);
	
	//Prepare method overrides.
	// 存在methodOverrides--存在 lookup-method 和 replace-method 
	// 如果一个类中存在若干个重载方法
	// 增强的时候还需要根据参数类型进行匹配
	// 如果当前类中的方法只有一个，那么需要被替换的的方法没有被重载
	// 后续调用的时候便可以直接使用找到的方法，而不需要进行方法的参数匹配验证了
	mbdToUse.prepareMethodOverrides();

    
//Give BeanPostProcessors a chance to return a proxy instead of the target bean instance 
//这里改变原始的bean变成代理的bean  
//将 AbsractBeanDefinition 转换为 BeanWrapper 前的处理
//子类一个修改 BeanDefinition 的机会
    
//实现BeanPostProcessor的实现类注册到IOC中
//所有的bean都经过bean的后置处理器BeanPostProcessor 
//分别在bean的初始化前后对bean对象提供自己的实例化逻辑
//postProcessAfterInitialization：初始化之后对bean进行增强处理
//postProcessBeforeInitialization：初始化之前对bean进行增强处理
   

//AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsBeforeInstantiation 
//InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation   
    
//AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization
//BeanPostProcessors.postProcessAfterInitialization  

// 这里改变原始的bean变成代理的bean 
3.Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
    // 如果返回的代理bean不等于null直接返回--不进行原始bean的创建
    // 我们熟知的 AOP 功能就是基于这里的判断的
    if (bean != null) {
        return bean;
    }

	// 对处理器中的所有 lnstantiationAwareBeanPostProcessor 类型的后处理器进行 
	// postProcessBeforelnstantiation 方法 和
	// BeanPostProcessor.postProcessAfterInitialization 方法的调用 
	
	// 经过applyBeanPostProcessorsBeforeInstantiation处理的bean
	// 可能有可能已经不是我们认为的 bean 了, 或许是一个经过处理的代理 bean，
	// 可能是通过 cglib 生成的，也可能是通过其他技术生成的
    bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
    if (bean != null) {
        // spring 到这里已经不会再去创建bean了
        // 所以要保证BeanPostProcessor.postProcessAfterInitialization 方法的调用
        // 只能在这里处理了
        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
    }
	return bean;


// 常规创建bean就是在doCreateBean里面完成--后面讲解
4.Object beanInstance = doCreateBean(beanName, mbdToUse, args);   
    
```



### FactoryBean 的使用

**当配置文件中＜bean＞的 class 属性配置的实现类是 FactoryBean 时，通过 getBean（）方法法返回的不是 FactoryBean 本身，而是 FactoryBean#getObject() 方法所返回的对象相当于  FactoryBean#getObject（）  代理了 getBean（）方法**

```java
public class CarFactoryBean implements FactoryBean<Car> {

    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public Car getObject() throws Exception {
        String[] infos = StringUtils.tokenizeToStringArray(info, ",");
        Car car = new Car();
        if (infos.length >= 3) {
            car.setBrand(infos[0]);
            car.setMaxSpeed(Integer.valueOf(infos[1]));
            car.setPrice(Double.valueOf(infos[2]));
        }
        return car;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }
}
```

```xml
<bean id="car" class="com.qin.demo.bean.CarFactoryBean">
    <property name="info" value="超级跑车,400,200000"></property>
</bean>
```

**当调用 getBean("car")时， Spring 通过反射机制发现 CarFactoryBean实现了 FactoryBean 的接口，这时 Spring 容器就调用接口方法 CarFactoryBean#getObject（）方法返回。 如果希望获取 CarFactoryBean 的实例，则需要在使用 getBean(beanName） 方法时在 beanName 前显示的加上 ”＆”前缀，例如 getBean（"＆car"）**



### 循环依赖

**spring 容器中只处理单例的 setter 循环依赖**

**对于构造器依赖和 prototype 范围的依赖不进行处理，直接抛出 BeanCurrentlyInCreationException**

**对于prototype 作用域的bean,spring无法完成依赖注入，因为 spring不缓存prototype 范围的bean**

**所以spring无法提前暴露创建中的bean**

**Setter 注入造成的依赖是通过 Spring 容器 提前暴露刚完成构造器注入但未完成其他步骤（如 setter 注入）的 bean 来完成的。通过提前暴露一个单例工厂方法，从而使其他 bean 能引用到 该 bean**

代码：在AbstractAutowireCapableBeanFactory.doCreateBean里面

```java
addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
```

**对于singleton作用域的bean可以通过beanFactory.setAllowCircularReferences(false);来禁止循环引用**



 prototype 范围和其他范围 的依赖通过 **prototypesCurrentlyInCreation** 池去判断

```java
/** Names of beans that are currently in creation */
private final ThreadLocal<Object> prototypesCurrentlyInCreation =
    new NamedThreadLocal<>("Prototype beans currently in creation");

// 在创建prototype和其他范围的依赖时都调用
beforePrototypeCreation(beanName);
afterPrototypeCreation(beanName);

//判断：当前线程内有没有构造器依赖和 prototype 范围的依赖
isPrototypeCurrentlyInCreation(String beanName)
```

singleton 范围的 有一个参数 **singletonsCurrentlyInCreation** 通过这个参数判断

```java
boolean earlySingletonExposure =
   mbd.isSingleton() && 
   this.allowCircularReferences &&
   isSingletonCurrentlyInCreation(beanName);
// 在DefaultSingletonBeanRegistry.isSingletonCurrentlyInCreation()
isSingletonCurrentlyInCreation(beanName) 判断是否提前加载

//在getSingleton()方法里
/** Names of beans that are currently in creation */
private final Set<String> singletonsCurrentlyInCreation =
    Collections.newSetFromMap(new ConcurrentHashMap<>(16));

beforeSingletonCreation(beanName);
afterSingletonCreation(beanName);

/**
 * singletonFactories 和 earlySingletonObjects 组成bean池
 * 两个是互斥的 
 * singletonFactories.getBean() 方法后
 * 删除自身 然后放到 earlySingletonObjects 里
 */

addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));

protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
    Assert.notNull(singletonFactory, "Singleton factory must not be null");
    synchronized (this.singletonObjects) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }
}

```



 构造器依赖：

- Spring 容器创建"testA"  bean，首先去"**当前创建 bean 池**"查找是否当前 bean 正在创建，如果没发现，则继续准备其需要的构造器参数"testB" ，并将"testA"标识符放到"当前创建 bean 池"
- Spring 容器创建"testB" bean， 首先去"**当前创建 bean 池**"查找是否当前 bean 正在创建，如果没发现，则继续准备其需要的构造器参数"testC"，并将"testB"标识符 放到“当前创建 bean 池"
- Spring 容器创建"testC" bean， 首先去"**当前创建 bean 池**"查找是否当前 bean 正在创建，如果没发现，则继续准备其需要的构造器参数"testA"，并将"testC"标识符 放到“当前创建 bean 池"
- 到此为止 Spring 容器要去创建"testA" bean，发现该 bean 标识符在"**当前创建 bean 池**"中，因为表示循环依赖，抛出 BeanCurrently InCreationException

setter 循环依赖：

- Spring 容器创建单例"testA" bean ，首先根据**无参构造器**创建 bean，并暴露一个"ObjectFactory" 用于返回一个提前暴露一个创建中的 bean，并将"testA"标识符放到"**当前创建 bean 池**", 然后进行 setter 注入"testB"
- addSingletonFactory （放入池中）  ->  populateBean(beanName, mbd, instanceWrapper) 属性注入
- Spring 容器创建单例"testB" bean ，首先根据**无参构造器**创建 bean，并暴露一个"ObjectFactory" 用于返回一个提前暴露一个创建中的 bean，并将"testB"标识符放到"**当前创建 bean 池**", 然后进行 setter 注入"testC"
- addSingletonFactory （放入池中）  ->  populateBean(beanName, mbd, instanceWrapper) 属性注入
- Spring 容器创建单例"testC" bean ，首先根据**无参构造器**创建 bean，并暴露一个"ObjectFactory" 用于返回一个提前暴露一个创建中的 bean，并将"testC"标识符放到"**当前创建 bean 池**", 然后进行 setter 注入"testA"
- addSingletonFactory （放入池中）  ->  populateBean(beanName, mbd, instanceWrapper) 属性注入
- 进行注入" testA" 时由于提前暴露了"ObjectFactory" 工厂，从而使用它返回提前暴露一个创建中的 bean。
- 最后在依赖注入 "testB"和"testA"，完成 setter 注入

对于prototype 作用域的依赖：

- 对于"prototype" 作用域的bean ,Spring 容器无法完成依赖注入，因为Spring 不缓存 "prototype" 作用域的bean ，因此无法提前暴露一个创建中的bean



### 创建 bean

经历过 resolveBeforelnstantiation 方法后，程序有两个选择  ，如果创建 了代理或者说重写了 InstantiationAwareBeanPostProcessor 的 postProcessBeforelnstantiation 方法并在方法 postProcessBeforelnstantiation 中改变了 bean 则直接返回就可以了 ， 否则需要进行常规 bean 的创建。 而 这常规 bean 的创建就是在 doCreateBean 中完成的

```java
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
			throws BeanCreationException {

		// Instantiate the bean.
		BeanWrapper instanceWrapper = null;
		if (mbd.isSingleton()) {
			instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
		}
		if (instanceWrapper == null) {
            //根据指定bean使用对应的策略创建新的实例 
            //如：工厂方法、构造函数自动注入 、简单初始化 
			instanceWrapper = createBeanInstance(beanName, mbd, args);
		}
		final Object bean = instanceWrapper.getWrappedInstance();
		Class<?> beanType = instanceWrapper.getWrappedClass();
		if (beanType != NullBean.class) {
			mbd.resolvedTargetType = beanType;
		}

		// Allow post-processors to modify the merged bean definition.
		synchronized (mbd.postProcessingLock) {
			if (!mbd.postProcessed) {
				try {
					applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
				}
				catch (Throwable ex) {
					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
							"Post-processing of merged bean definition failed", ex);
				}
				mbd.postProcessed = true;
			}
		}

		// Eagerly cache singletons to be able to resolve circular references
		// even when triggered by lifecycle interfaces like BeanFactoryAware.
    	// 是否需要提前曝光：单例&允许循环依赖&当前正在创建中，检查循环依赖
		boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
				isSingletonCurrentlyInCreation(beanName));
		if (earlySingletonExposure) {
			if (logger.isDebugEnabled()) {
				logger.debug("Eagerly caching bean '" + beanName +
						"' to allow for resolving potential circular references");
			}
			addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
		}

		// Initialize the bean instance.
		Object exposedObject = bean;
		try {
            // 属性填充
			populateBean(beanName, mbd, instanceWrapper);
            // 调研始化方法，比如 init- method 
			exposedObject = initializeBean(beanName, exposedObject, mbd);
		}
		catch (Throwable ex) {
			if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
				throw (BeanCreationException) ex;
			}
			else {
				throw new BeanCreationException(
						mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
			}
		}

		if (earlySingletonExposure) {
			Object earlySingletonReference = getSingleton(beanName, false);
            // earlySingletonReference 只有在检测到有循环依赖的忻况下才会不为空 
			if (earlySingletonReference != null) {
                // 如果 exposedObject 没有在初始化方法中被改变，也就是没有被增强 
				if (exposedObject == bean) {
					exposedObject = earlySingletonReference;
				}
				else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
					String[] dependentBeans = getDependentBeans(beanName);
					Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
					for (String dependentBean : dependentBeans) {
						if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
							actualDependentBeans.add(dependentBean);
						}
					}
					if (!actualDependentBeans.isEmpty()) {
						throw new BeanCurrentlyInCreationException(beanName,
								"Bean with name '" + beanName + "' has been injected into other beans [" +
								StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
								"] in its raw version as part of a circular reference, but has eventually been " +
								"wrapped. This means that said other beans do not use the final version of the " +
								"bean. This is often the result of over-eager type matching - consider using " +
								"'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
					}
				}
			}
		}

		// Register bean as disposable.
		try {
            // 根据 scopse 注册 bean （注册销毁方法）
			registerDisposableBeanIfNecessary(beanName, bean, mbd);
		}
		catch (BeanDefinitionValidationException ex) {
			throw new BeanCreationException(
					mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
		}

		return exposedObject;
	}
```

createBeanInstance ：使用适当的实例化策略为指定的bean创建一个新实例:工厂方法、构造函数自动装配或简单实例化

```java
/**
  * 使用适当的实例化策略为指定的bean创建一个新实例:工厂方法、构造函数自动装配或简单实例化。
  *
  * @param beanName bean的名称
  * @param mbd      bean的bean定义
  * @param args     用于构造函数或工厂方法调用的显式参数
  * @return 新实例的BeanWrapper
  * @see #obtainFromSupplier
  * @see #instantiateUsingFactoryMethod
  * @see #autowireConstructor
  * @see #instantiateBean
  */
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
		// Make sure bean class is actually resolved at this point.
    	// 确保此时bean类已经被解析。
		Class<?> beanClass = resolveBeanClass(mbd, beanName);
		// 检测beanClass是不是公共的，是否允许非公共访问的构造器和方法
		if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
			throw new BeanCreationException(mbd.getResourceDescription(), beanName,
					"Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
		}
    	// 通过实例提供者实例化（Spring5新增的实例化策略）
    	// 如果存在 Supplier 回调，则调用 obtainFromSupplier() 进行初始化
		// Spring5.0新增的实例化策略,如果设置了该策略,将会覆盖构造方法和工厂方法实例化策略
		Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
		if (instanceSupplier != null) {
			return obtainFromSupplier(instanceSupplier, beanName);
		}
		// 如果有工厂方法的话,则使用工厂方法实例化bean
		if (mbd.getFactoryMethodName() != null) {
			return instantiateUsingFactoryMethod(beanName, mbd, args);
		}
		
		// Shortcut when re-creating the same bean...
    	// 当创建一个相同的bean时,使用之前保存的快照
    	// 单例模式: IoC容器除了可以获取Bean之外,还能销毁Bean
    	// 		当我们调用xmlBeanFactory.destroyBean(myBeanName,myBeanInstance)
    	// 		销毁bean时,容器是不会销毁已经解析的构造函数快照的,如果再次调用
        // 		xmlBeanFactory.getBean(myBeanName)时,就会使用该策略了 
    	// 原型模式: 对于该模式的理解就简单了
    	//		IoC容器不会缓存原型模式bean的实例
    	// 		当我们第二次向容器索取同一个bean时,就会使用该策略了
    	// 解析构造函数是一个比较消耗性能的步骤，所以采取缓存机制，
    	// 如果已经解析过则,不需要重复解析而是直接从 
    	// RootBeanDefinition 中的属性resolvedConstructorOrFactoryMethod缓存中获取
    	// 否则需要再次解析
    	// resolvedConstructorOrFactoryMethod 存储的就是实例化bean的构造器
    	boolean resolved = false;
		boolean autowireNecessary = false;
		if (args == null) {
			synchronized (mbd.constructorArgumentLock) {
				if (mbd.resolvedConstructorOrFactoryMethod != null) {
					resolved = true;
					autowireNecessary = mbd.constructorArgumentsResolved;
				}
			}
		}
    	// 如果该bean已经被解析过
		if (resolved) {
            // 使用已经解析过的构造函数实例化
			if (autowireNecessary) {
				return autowireConstructor(beanName, mbd, null, null);
			}
            // 使用默认无参构造函数实例化
			else {
				return instantiateBean(beanName, mbd);
			}
		}

		// Candidate constructors for autowiring?
    	// 通过BeanPostProcessors确定需要使用的构造函数
		Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
		if (ctors != null || mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR ||
				mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
            // args 显式指定参数 - factory.getBean("cat","美美",3);
			return autowireConstructor(beanName, mbd, ctors, args);
		}

		// No special handling: simply use no-arg constructor.
    	// 无任何的特殊处理,则使用默认的无参构造函数实例化bean
		return instantiateBean(beanName, mbd);
	}
```

从该方法里我们看到了Spring实例化bean的策略:

- **工厂方法（实例工厂和静态工厂）**
- **构造函数实例化（无参构造和有参构造）**
- **通过实例提供者实例化（Spring5新增的实例化策略）**

###  无参构造函数实例化Bean

instantiateBean(beanName, mbd);

```java
/**
 * 创建实例使用默认的无参构造器
 */
protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
    try {
        Object beanInstance;
        final BeanFactory parent = this;
        // 1、如果权限管理器不为空,需要校验
        if (System.getSecurityManager() != null) {
            beanInstance = AccessController.doPrivileged((PrivilegedAction<Object>) () ->
                                                         getInstantiationStrategy().instantiate(mbd, beanName, parent),
                                                         getAccessControlContext());
        }
        else {
            // 2、获取实例化策略并实例化bean
            // Spring实例化bean的两种策略: CGLIB动态代理来创建对象实例 和 JDK的反射机制
            // 默认：new CglibSubclassingInstantiationStrategy();
            // 如果没有使用方法覆盖(replace-method或lookup-method注入)
            // 则使用反射创建bean的实例,
            // 否则必须使用CGLIB机制
			// if (!bd.hasMethodOverrides()) {}
            beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
        }
        // 3、实例并初始化BeanWrapper对象
        BeanWrapper bw = new BeanWrapperImpl(beanInstance);
        initBeanWrapper(bw);
        return bw;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
            mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
    }
}
```

```java
@Override
public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
    // Don't override the class with CGLIB if no overrides.
    // 没有使用方法覆盖(replace-method或lookup-method注入)就用JDK反射机制创建实例
    if (!bd.hasMethodOverrides()) {
        Constructor<?> constructorToUse;
        synchronized (bd.constructorArgumentLock) {
            constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;
            if (constructorToUse == null) {
                final Class<?> clazz = bd.getBeanClass();
                if (clazz.isInterface()) {
                    throw new BeanInstantiationException(clazz, "Specified class is an interface");
                }
                try {
                    if (System.getSecurityManager() != null) {
                        constructorToUse = AccessController.doPrivileged(
                            (PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
                    }
                    else {
                        constructorToUse = clazz.getDeclaredConstructor();
                    }
                    bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                }
                catch (Throwable ex) {
                    throw new BeanInstantiationException(clazz, "No default constructor found", ex);
                }
            }
        }
        // JDk反射--构造器 ctor.newInstance(args) 创建实例 args ==null 无参构造器
        return BeanUtils.instantiateClass(constructorToUse);
    }
    else {
        // Must generate CGLIB subclass.
        // 使用cglib增强 -- 无参构造器
        // 
        return instantiateWithMethodInjection(bd, beanName, owner);
    }
}
```

jdk 反射创建实例****

```java
// jdk 反射创建实例
public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
	Assert.notNull(ctor, "Constructor must not be null");
    // 使构造器可以被访问
    ReflectionUtils.makeAccessible(ctor);
    // 从Spring Boot 2开始，Boot也开始正式支持Kotlin编程，
    // KotlinDetector，Spring5.0新增的类，用于检测Kotlin的存在和识别Kotlin类型
    // 我们可以在创建Spring Boot应用时程序时使用Spring初始化Kotlin
    // 不过Kotlin要在新的Spring 5版本中才得到支持
	return (KotlinDetector.isKotlinType(ctor.getDeclaringClass()) ?
					KotlinDelegate.instantiateClass(ctor, args) : ctor.newInstance(args));
    
```

**cglib创建bean实例：**

```java
/**
 * 动态创建一个实例（实现了lookups和replace的子类）
 */
public Object instantiate(@Nullable Constructor<?> ctor, @Nullable Object... args) {
    // 对类进行增强
    Class<?> subclass = createEnhancedSubclass(this.beanDefinition);
    Object instance;
    if (ctor == null) {
        // 对增强的类进行创建
        instance = BeanUtils.instantiateClass(subclass);
    } else {
        try {
            // 获取增强类的构造器
            Constructor<?> enhancedSubclassConstructor = subclass.getConstructor(ctor.getParameterTypes());
            // 对增强的类--创建有构造器参数的实例
            instance = enhancedSubclassConstructor.newInstance(args);
        } catch (Exception ex) {
            throw new BeanInstantiationException(this.beanDefinition.getBeanClass(),
                                                 "Failed to invoke constructor for CGLIB enhanced subclass [" + subclass.getName() + "]", ex);
        }
    }
    // SPR-10785: set callbacks directly on the instance instead of in the
    // enhanced class (via the Enhancer) in order to avoid memory leaks.
    // 设置回调
    Factory factory = (Factory) instance;
    factory.setCallbacks(new Callback[] {NoOp.INSTANCE,
                                             new 		LookupOverrideMethodInterceptor(this.beanDefinition, this.owner),
                                             new ReplaceOverrideMethodInterceptor(this.beanDefinition, this.owner)});
        return instance;
        
} 
```

```java
// cglib 增强的类
private static final Class<?>[] CALLBACK_TYPES = new Class<?>[]
				{NoOp.class, LookupOverrideMethodInterceptor.class, ReplaceOverrideMethodInterceptor.class};

NoOp.class : 调用原始的方法
LookupOverrideMethodInterceptor.class:用增强的结果替换返回结果
ReplaceOverrideMethodInterceptor.class: 替换方法的执行

private Class<?> createEnhancedSubclass(RootBeanDefinition beanDefinition) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(beanDefinition.getBeanClass());
    enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
    if (this.owner instanceof ConfigurableBeanFactory) {
        ClassLoader cl = ((ConfigurableBeanFactory) this.owner).getBeanClassLoader();
        enhancer.setStrategy(new ClassLoaderAwareGeneratorStrategy(cl));
    }
    enhancer.setCallbackFilter(new MethodOverrideCallbackFilter(beanDefinition));
    enhancer.setCallbackTypes(CALLBACK_TYPES);
    return enhancer.createClass();
}
```

### 有参构造方法实例化单例bean

ConstructorResolver.autowireConstructor()

```java
public BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd,
			@Nullable Constructor<?>[] chosenCtors, @Nullable Object[] explicitArgs) {

    BeanWrapperImpl bw = new BeanWrapperImpl();
    this.beanFactory.initBeanWrapper(bw);

    Constructor<?> constructorToUse = null;
    ArgumentsHolder argsHolderToUse = null;
    Object[] argsToUse = null;
	// 判断有无显式指定参数,如果有则优先使用,如xmlBeanFactory.getBean("cat", "美美",3);
    if (explicitArgs != null) {
        argsToUse = explicitArgs;
    } else {
        // 从配置文件中解析
        Object[] argsToResolve = null;
        synchronized (mbd.constructorArgumentLock) {
            // 先缓存的构造器 -- 解析过程是比较复杂也耗时的
            // 缓存中缓存的可能是参数的最终类型也可能是参数的初始类型
            constructorToUse = (Constructor<?>) mbd.resolvedConstructorOrFactoryMethod;
            // 缓存中没有找到构造器且构造器解析标识也是false
            if (constructorToUse != null && mbd.constructorArgumentsResolved) {
                // Found a cached constructor...
                // 缓存中缓存的可能是参数的最终类型也可能是参数的初始类型，
                // 例如：构造函数参数要求的是 int 类型，但是原始的参数值可能是 String 类型的“l” ，
                // 那么即使在缓存中得到了参数，
                // 也需要经过类型转换器的过滤以确保参数类型与对应的构造函数参数类型完全对应
                argsToUse = mbd.resolvedConstructorArguments;
                if (argsToUse == null) {
                    // 解析配置文件后的构造涵数参数 
                    // 就是String "1" 转成 int 1 后的参数
                    argsToResolve = mbd.preparedConstructorArguments;
                }
            }
        }
        // 如果缓存中存在 
        if (argsToResolve != null) {
            // 解析参数类型， 如给定方法的构造函数 A( int , int ） 则通过此方法后就会把配置中的 
            //（ ”1”，”l”）转换为 (1 , 1) 
            // 缓存中的值可能是原始值也可能是最终值
            argsToUse = resolvePreparedArguments(beanName, mbd, bw, constructorToUse, argsToResolve);
        }
    }
	
    // 缓存中没有数据
    if (constructorToUse == null) {
        // Need to resolve the constructor.
        // determineConstructorsFromBeanPostProcessors 里面没有找到
        // BeanPostProcessors 的 determineCandidateConstructors 构造器
        // SmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors
        boolean autowiring = (chosenCtors != null ||
       mbd.getResolvedAutowireMode() == AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR);
        
        ConstructorArgumentValues resolvedValues = null;
		
        // 这里定义了一个变量,来记录最小的构造函数参数个数,其作用可以参见下面解释
        int minNrOfArgs;
        if (explicitArgs != null) {
            minNrOfArgs = explicitArgs.length;
        } else {
            // 提取配置文件的构造器参数
            ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
            // 用于承载解析后的构造器参数
            resolvedValues = new ConstructorArgumentValues();
            // 能解析到的参数个数
            minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
        }

        // Take specified constructors, if any.
        // 这里是解析SmartInstantiationAwareBeanPostProcessor得出的构造函数
        // 参见AbstractAutowireCapableBeanFactory 类的
        // .determineConstructorsFromBeanPostProcessors(beanClass, beanName)
        Constructor<?>[] candidates = chosenCtors;
        if (candidates == null) {
            Class<?> beanClass = mbd.getBeanClass();
            try {
                // 如果没有指定的构造函数,则根据方法访问级别,获取该bean所有的构造函数
                // 如果不能访问就获取：beanClass.getDeclaredConstructors() 
                // 	 就获取所有的构造器方法包括public，protected，private和默认的 
                // 如果能访问就获取：beanClass.getConstructors()就获取 public 的构造器 
                // 注意:该处获取到的构造函数,并不是配置文件中定义的构造函数,而是bean类中的构造函数
                candidates = (mbd.isNonPublicAccessAllowed() ?
                              beanClass.getDeclaredConstructors() : beanClass.getConstructors());
            }
            catch (Throwable ex) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                                "Resolution of declared constructors on bean Class [" + beanClass.getName() +
                                                "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
            }
        }
        
        // 排序给定的构造函数 public 构造函数优先参数数量降序
        // 非 public 构造函数参数数量降序 
        AutowireUtils.sortConstructors(candidates);
        
        int minTypeDiffWeight = Integer.MAX_VALUE;
        Set<Constructor<?>> ambiguousConstructors = null;
        LinkedList<UnsatisfiedDependencyException> causes = null;

        for (Constructor<?> candidate : candidates) {
            // 获取构造器的参数
            Class<?>[] paramTypes = candidate.getParameterTypes();
			// 已经获取构造器，且 需要的参数个数小于当前的构造函数参数个数则终止 
            // 因为已经按照参数个数排序
            if (constructorToUse != null && argsToUse.length > paramTypes.length) {
                // Already found greedy constructor that can be satisfied ->
                // do not look any further, there are only less greedy constructors left.
                break;
            }
            // public 的已经解析完成 解析 private 的
            // 如果从bean类中解析到的构造函数个数小于从beanDefinition中解析到的构造函数个数
            // 那么肯定不会使用该方法实例化,循环继续
            // 简单的理解:beanDefinition中的构造函数和bean类中的构造函数参数个数不相等,
            //		那么肯定不会使用该构造函数实例化
            if (paramTypes.length < minNrOfArgs) {
                continue;
            }

            ArgumentsHolder argsHolder;
            if (resolvedValues != null) {
                try {
                    /*从@ConstructorPropertie里获取参数
                     *public class Point {
                           @ConstructorProperties({"x", "y"})
                           public Point(int x, int y) {
                               this.x = x;
                               this.y = y;
                           }
                           private final int x, y;
                       }
                     *
                     */
                    String[] paramNames = ConstructorPropertiesChecker.evaluate(candidate, paramTypes.length);
                    if (paramNames == null) {
                        ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
                        if (pnd != null) {
                            // 
                            paramNames = pnd.getParameterNames(candidate);
                        }
                    }
                    argsHolder = createArgumentArray(beanName, mbd, resolvedValues, bw, paramTypes, paramNames,
                                                     getUserDeclaredConstructor(candidate), autowiring);
                }
                catch (UnsatisfiedDependencyException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Ignoring constructor [" + candidate + "] of bean '" + beanName + "': " + ex);
                    }
                    // Swallow and try next constructor.
                    if (causes == null) {
                        causes = new LinkedList<>();
                    }
                    causes.add(ex);
                    continue;
                }
            }
            else {
                // Explicit arguments given -> arguments length must match exactly.
                if (paramTypes.length != explicitArgs.length) {
                    continue;
                }
                argsHolder = new ArgumentsHolder(explicitArgs);
            }

            int typeDiffWeight = (mbd.isLenientConstructorResolution() ?
                                  argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes));
            // Choose this constructor if it represents the closest match.
            if (typeDiffWeight < minTypeDiffWeight) {
                constructorToUse = candidate;
                argsHolderToUse = argsHolder;
                argsToUse = argsHolder.arguments;
                minTypeDiffWeight = typeDiffWeight;
                ambiguousConstructors = null;
            }
            else if (constructorToUse != null && typeDiffWeight == minTypeDiffWeight) {
                if (ambiguousConstructors == null) {
                    ambiguousConstructors = new LinkedHashSet<>();
                    ambiguousConstructors.add(constructorToUse);
                }
                ambiguousConstructors.add(candidate);
            }
        }

        if (constructorToUse == null) {
            if (causes != null) {
                UnsatisfiedDependencyException ex = causes.removeLast();
                for (Exception cause : causes) {
                    this.beanFactory.onSuppressedException(cause);
                }
                throw ex;
            }
            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                            "Could not resolve matching constructor " +
                                            "(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities)");
        }
        else if (ambiguousConstructors != null && !mbd.isLenientConstructorResolution()) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                            "Ambiguous constructor matches found in bean '" + beanName + "' " +
                                            "(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " +
                                            ambiguousConstructors);
        }

        if (explicitArgs == null) {
            argsHolderToUse.storeCache(mbd, constructorToUse);
        }
    }

    try {
        final InstantiationStrategy strategy = beanFactory.getInstantiationStrategy();
        Object beanInstance;

        if (System.getSecurityManager() != null) {
            final Constructor<?> ctorToUse = constructorToUse;
            final Object[] argumentsToUse = argsToUse;
            beanInstance = AccessController.doPrivileged((PrivilegedAction<Object>) () ->
                                                         strategy.instantiate(mbd, beanName, beanFactory, ctorToUse, argumentsToUse),
                                                         beanFactory.getAccessControlContext());
        }
        else {
            beanInstance = strategy.instantiate(mbd, beanName, this.beanFactory, constructorToUse, argsToUse);
        }

        bw.setBeanInstance(beanInstance);
        return bw;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                        "Bean instantiation via constructor failed", ex);
    }
}
```





