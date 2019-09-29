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



**ApplicationListener** 

是Spring事件机制的一部分，与抽象类ApplicationEvent类配合来完成ApplicationContext的事件机制。

如果容器中存在ApplicationListener的Bean，当ApplicationContext调用publishEvent方法时，对应的Bean会被触发。这一过程是典型的观察者模式的实现

**ContextRefreshedEvent事件的监听** 

以Spring的内置事件ContextRefreshedEvent为例，当ApplicationContext被初始化或刷新时，会触发ContextRefreshedEvent事件，下面我们就实现一个ApplicationListener来监听此事件的发生。

```java
@Component // 需对该类进行Bean的实例化
public class LearnListener implements ApplicationListener<ContextRefreshedEvent> {
   @Override
   public void onApplicationEvent(ContextRefreshedEvent event) {
      // 打印容器中出事Bean的数量
      System.out.println("监听器获得容器中初始化Bean数量：" + 	event.getApplicationContext().getBeanDefinitionCount());
   }
}
```

**自定义事件及监听**

```java
public class NotifyEvent extends ApplicationEvent {

    private String email;

    private String content;

    public NotifyEvent(Object source) {
        super(source);
    }

    public NotifyEvent(Object source, String email, String content) {
        super(source);
        this.email = email;
        this.content = content;
    }
    // 省略getter/setter方法
}
```

定义监听器NotifyListener：

```java
@Component
public class NotifyListener implements ApplicationListener<NotifyEvent> {

    @Override
    public void onApplicationEvent(NotifyEvent event) {
        System.out.println("邮件地址：" + event.getEmail());
        System.out.println("邮件内容：" + event.getContent());
    }
}


```

单元测试类：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ListenerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testListener() {

        NotifyEvent event = new NotifyEvent("object", "abc@qq.com", "This is the content");

        webApplicationContext.publishEvent(event);
    }
}
```



### ApplicationContext创建过程简析

```java
public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
    this(new String[] {configLocation}, true, null);
}

public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, @Nullable ApplicationContext parent) throws BeansException {
    // 调用父类构造器
    super(parent);
    // 设定配置文件路径
    // 此函数主要用于解析给定的路径数组，
    // 当然， 如果数组中包含特殊符号，如$｛var｝,
    // 那么 在 resolvePath 中会搜寻匹配的系统变量并替换
    setConfigLocations(configLocations);
    if (refresh) {
        // 扩展功能
        refresh();
    }
}

AbstractApplicationContext.refresh()

@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        // 1、准备刷新上下文环境
        // 例如对系统属性或者环境变量进行准备及验证
        prepareRefresh();

        // Tell the subclass to refresh the internal bean factory.
        // 2、obtainFreshBeanFactory->读取xml并初始化BeanFactory
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // Prepare the bean factory for use in this context.
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            postProcessBeanFactory(beanFactory);

            // Invoke factory processors registered as beans in the context.
            invokeBeanFactoryPostProcessors(beanFactory);

            // Register bean processors that intercept bean creation.
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            initMessageSource();

            // Initialize event multicaster for this context.
            initApplicationEventMulticaster();

            // Initialize other special beans in specific context subclasses.
            onRefresh();

            // Check for listener beans and register them.
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            finishBeanFactoryInitialization(beanFactory);

            // Last step: publish corresponding event.
            finishRefresh();
        } catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                            "cancelling refresh attempt: " + ex);
            }

            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();

            // Reset 'active' flag.
            cancelRefresh(ex);

            // Propagate exception to caller.
            throw ex;
        }

        finally {
            // Reset common introspection caches in Spring's core, since we
            // might not ever need metadata for singleton beans anymore...
            resetCommonCaches();
        }
    }
}
```



### prepareRefresh刷新上下文的准备工作

```java
/**
 * 准备刷新上下文环境，设置它的启动日期和活动标志，以及执行任何属性源的初始化。
 * Prepare this context for refreshing, setting its startup date and
 * active flag as well as performing any initialization of property sources.
 */
protected void prepareRefresh() {
    	// Switch to active.
        this.startupDate = System.currentTimeMillis();
        this.closed.set(false);
        this.active.set(true);

        if (logger.isInfoEnabled()) {
            logger.info("Refreshing " + this);
        }

        // Initialize any placeholder property sources in the context environment.
        // 在上下文环境中初始化任何占位符属性源。(空的方法,留给子类覆盖)
    	// spring 给用户的扩展 
    	/**
    	 * 继承自 ClassPathXmlApplicationContext 的 MyClassPathXmlApplicationContext
    	 * 重写 initPropertySources() 
    	 * 默认 getEnvironment() 获取到 systemProperties 和 systemEnvironment
    	 * 重写：getEnvironment().setRequiredProperties("java.version");
    	 * 那么：getEnvironment().validateRequiredProperties();就可以检测是否有对应得环境变量
    	 */
        initPropertySources();

        // Validate that all properties marked as required are resolvable:
        // see ConfigurablePropertyResolver#setRequiredProperties
        // 验证需要的属性文件是否都已放入环境中
    	// 查看 ConfigurablePropertyResolver#setRequiredProperties
        getEnvironment().validateRequiredProperties();
		
    
    	// earlyApplicationListeners，applicationListeners，earlyApplicationEvents 初始化
    	// earlyApplicationListeners 不等于空就需要请空 applicationListeners
    	// 把 earlyApplicationListeners 添加到 applicationListeners中
    	/** 
    	 * applicationListeners 和 ApplicationEvents 配合使用 
    	 * 观察者模式
    	 */
        // Store pre-refresh ApplicationListeners...
        if (this.earlyApplicationListeners == null) {
            this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
        }
        else {
            // Reset local application listeners to pre-refresh state.
            this.applicationListeners.clear();
            this.applicationListeners.addAll(this.earlyApplicationListeners);
        }

        // Allow for the collection of early ApplicationEvents,
        // to be published once the multicaster is available...
        this.earlyApplicationEvents = new LinkedHashSet<>();
}



/**
 * 添加容器必要的变量
 */
public void setRequiredProperties(String... requiredProperties) {
    for (String key : requiredProperties) {
        this.requiredProperties.add(key);
    }
}

/**
 * 校验元素是否存在
 */
public void validateRequiredProperties() {
    MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
    for (String key : this.requiredProperties) {
        if (this.getProperty(key) == null) {
            ex.addMissingRequiredProperty(key);
        }
    }
    if (!ex.getMissingRequiredProperties().isEmpty()) {
        throw ex;
    }
}

/**
 * 功能：修改ClassPathXmlApplicationContext 
 */
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {
	
    // 默认有系统变量 systemProperties （ file.encoding -> UTF-8 java.version -> 1.8.0_221）
    // 和系统环境 systemEnvironment （OS -> Windows_NT ，JAVA_HOME -> D:\Java\jdk1.8.0_221）
    
    @Override
    protected void initPropertySources() {
        // 默认 new StandardEnvironment() 
        getEnvironment().setRequiredProperties("java.version");
    }
}


```



### obtainFreshBeanFactory->读取xml并初始化BeanFactory

```java
// 获取新鲜的 BeanFactory
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    // 刷新 BeanFactory 已经存在就销毁 然后 重建 放到this.beanFactory 里
    refreshBeanFactory();
    // 获取 this.beanFactory 的值
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    if (logger.isDebugEnabled()) {
    	logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
    }
    return beanFactory;
}


@Override
protected final void refreshBeanFactory() throws BeansException {
    // 1、如果BeanFactory的实例已经存在,则销毁并关闭
    if (hasBeanFactory()) {
        destroyBeans();
        closeBeanFactory();
    }
    // 2、重新创建BeanFactory
    try {
        // 创建 DefaultListableBeanFactory
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // 为了序列化指定ID，如果需要的话，让这个 BeanFactory 从 id 反序列到 BeanFactory 对象 
        // getId() = ObjectUtils.identityToString(this); 当前对象的toString
        // 放到 serializableFactories 里面
        beanFactory.setSerializationId(getId());
        // 定制 beanFactory， 设置相关属性， 
        // 包括是否允许覆盖同名称的不同定义的对象 和 循环依赖是否允许存在 
        // 设置＠Autowired 和自Qualifier 注解解析器
        // QualifierAnnotationAutowireCandidateResolver 
        customizeBeanFactory(beanFactory);
        loadBeanDefinitions(beanFactory);
        synchronized (this.beanFactoryMonitor) {
            this.beanFactory = beanFactory;
        }
    }
    catch (IOException ex) {
        throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
    }
}
```























