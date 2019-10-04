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
		
        // 到这里Spring 已经完成对配置的解析
        // ---------------------------------//
        
        // Prepare the bean factory for use in this context.
        // 3、填充BeanFactory功能
        // ApplicationContext 在功能上的扩展也由此展开
        // 注册一些默认的属性编辑器
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            // 4、该方法是个空的模板方法 
            // 所以的bean definitions 已经被加载 ，在实例化之前给一个机会 处理一下 
            postProcessBeanFactory(beanFactory);

            // Invoke factory processors registered as beans in the context.
            // 5、调动工厂的处理器在注册 Bean到容器 之前 
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
        // 1.包括是否允许覆盖同名称的不同定义的对象 和 2.循环依赖是否允许存在 
        // 设置＠Autowired 和自Qualifier 注解解析器
        // QualifierAnnotationAutowireCandidateResolver 
        customizeBeanFactory(beanFactory);
         // 加载BeanDefinition
        loadBeanDefinitions(beanFactory);
        synchronized (this.beanFactoryMonitor) {
            this.beanFactory = beanFactory;
        }
    }
    catch (IOException ex) {
        throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
    }
}

/**
 * 如果想设置 allowBeanDefinitionOverriding 和 allowCircularReferences
 * 使用子类覆 盖方法：
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        super.setAllowBeanDefinitionOverriding(false);
        super.setAllowCircularReferences(false);
        super.customizeBeanFactory(beanFactory);
    }
}
 * 
 */
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
    if (this.allowBeanDefinitionOverriding != null) {
        beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
    }
    if (this.allowCircularReferences != null) {
        beanFactory.setAllowCircularReferences(this.allowCircularReferences);
    }
}


@Override
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
    // Create a new XmlBeanDefinitionReader for the given BeanFactory.
    // 为指定 beanFactory 创建 XrnlBeanDefinitionReader 
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

    // Configure the bean definition reader with this context's
    // resource loading environment.
    // 对 beanDefinitionReader 进行环境变茧的设恒
    beanDefinitionReader.setEnvironment(this.getEnvironment());
    beanDefinitionReader.setResourceLoader(this);
    beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

    // Allow a subclass to provide custom initialization of the reader,
    // then proceed with actually loading the bean definitions.
    // 对 BeanDefinitionReader 进行设置，可以被盖 
    initBeanDefinitionReader(beanDefinitionReader);
    // 真正读取配置文件的方法
    loadBeanDefinitions(beanDefinitionReader);
}
```



### 填充BeanFactory功能

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    
    // Tell the internal bean factory to use the context's class loader etc.
    // 设置类加载器
    beanFactory.setBeanClassLoader(getClassLoader());
    
    // 设置beanFactory的表达式语言处理器,主要用于解析依赖注入 bean 的时候
    // Spring3开始增加了对语言表达式的支持,默认可以使用#{bean.xxx}的形式来调用相关属性值
    // 调用：AbstractAutowireCapableBeanFactory 类的 applyPropertyValues 函数
    //  evaluate(typedStringValue); 
    // 和构造器解析值得时候都用到了：evaluateBeanDefinitionString 
    //if (argValue instanceof String) {
	//	argValue = this.beanFactory.evaluateBeanDefinitionString((String) argValue, mbd);
	//} 
    beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
    
    // 为beanFactory增加一个默认的propertyEditor,这个主要是对bean的属性等设置管理的一个工具
    beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

    // Configure the bean factory with context callbacks.
   	// 添加ApplicationContextAwareProcessor
    // 这BeanPostProcessor 个处理了Aware接口
    /**
     private void invokeAwareInterfaces(Object bean) {
		if (bean instanceof Aware) {
			if (bean instanceof EnvironmentAware) {
				((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
			}
			if (bean instanceof EmbeddedValueResolverAware) {
				((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
			}
			if (bean instanceof ResourceLoaderAware) {
				((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
			}
			if (bean instanceof ApplicationEventPublisherAware) {
				((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
			}
			if (bean instanceof MessageSourceAware) {
				((MessageSourceAware) bean).setMessageSource(this.applicationContext);
			}
			if (bean instanceof ApplicationContextAware) {
				((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
			}
		}
	}
	*/
    // 在spring实例后，会调用 initializeBean 初始化方法 initializeBean 
    // 里面会调用 invokeAwareMethods 方法 
    // 简单讲：就是实例化 Aware接口 实例的时候调用 上面的set方法 获取	一些对应的资源
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    
    // 设置忽略自动装配的接口
    // 上面的 Aware 类已经不是普通的 bean 
    // 需要在 Spring 做 bean 的依赖注入的时候忽略它们。 
    // 而 ignoreDependencyInterface 的作用正是在此。
    
    // 实例化Aware接口的Bean就可以获取对应得资源 
    // 里面的EnvironmentAware,EmbeddedValueResolverAware，ResourceLoaderAware
    // ApplicationEventPublisherAware,MessageSourceAware,ApplicationContextAware
    // 6个属性不依赖注入
	/**

public class HelloApplicationContextAware implements ApplicationContextAware {
	// 这个不依赖注入
    private EnvironmentAware environmentAware;

    public void setEnvironmentAware(EnvironmentAware environmentAware) {
        this.environmentAware = environmentAware;
    }
	// 这个依赖注入
    private MyBean myBean;

    public void setMyBean(MyBean myBean) {
        this.myBean = myBean;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public void testAware() {
        // 通过 hello 这个 bean id 从 beanFactory 获取实例
        Hello hello = applicationContext.getBean("hello", Hello.class);
        hello.say();
        System.out.println(myBean.getDateValue());
        System.out.println(environmentAware.toString());
    }
}
	*/
    beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
    beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
    beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
    beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

    // BeanFactory interface not registered as resolvable type in a plain factory.
    // MessageSource registered (and found for autowiring) as a bean.
    // 设置几个自动装配的特殊规则
    // 注册依赖 
    // 当 bean 的属性注 入的时候， 
    // 一旦检测到属性为 BeanFactory 类型便会将 beanFactory 的实例注入进去
    // BeanFactory,ResourceLoader,ApplicationEventPublisher,ApplicationContext 
    // 上面4个作为Bean的属性时，直接注入
    beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
    beanFactory.registerResolvableDependency(ResourceLoader.class, this);
    beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
    beanFactory.registerResolvableDependency(ApplicationContext.class, this);
		
    // Register early post-processor for detecting inner beans as ApplicationListeners.
    // 添加ApplicationListener接口到AbstractApplicationContext.applicationListeners里面
    // 注册检测到的 ApplicationListener 接口
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

    
    // Detect a LoadTimeWeaver and prepare for weaving, if found.
    // 增加对AspectJ的支持
    if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        // Set a temporary ClassLoader for type matching.
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }

    
    // Register default environment beans.
    // 注册默认的系统环境bean
    // containsLocalBean 忽略父类的 bean 只看本容器的情况
    if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
    }
}
```



#### invokeBeanFactoryPostProcessors–>调用BeanFactoryPostProcessor













