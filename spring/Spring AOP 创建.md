### Spring AOP 的创建

通过自定义标签 <aop:aspectj-autoproxy > 的解析（AspectJAutoProxyBeanDefinitionParser.parse() ）对 AnnotationAwareAspectJAutoProxyCreator 类型的自动注册

```java
@Override
@Nullable
public BeanDefinition parse(Element element, ParserContext parserContext) {
    // 1、注册AnnotationAwareAspectJAutoProxyCreator
    AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
    // 2、处理子标签<aop:include/>， 指定@Aspect类，支持正则表达式，符合该表达式的切面类才会被应用
    extendBeanDefinition(element, parserContext);
    return null;
}

public static void registerAspectJAnnotationAutoProxyCreatorIfNecessary(
			ParserContext parserContext, Element sourceElement) {
	// 注册 AnnotationAwareAspectJAutoProxyCreator 的 BeanDefinition信息
    // 这个beanName= org.springframework.aop.config.internalAutoProxyCreator
    BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(
        parserContext.getRegistry(), parserContext.extractSource(sourceElement));
    
    // 2、解析子标签 proxy-target-class 和 expose-proxy
    useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
    // 3、注册组件并发送组件注册事件，便于监听器做进一步处理
    registerComponentIfNecessary(beanDefinition, parserContext);
}

@Nullable
public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(
    BeanDefinitionRegistry registry, @Nullable Object source) {
	// 注册 AnnotationAwareAspectJAutoProxyCreator
    return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
}

@Nullable
private static BeanDefinition registerOrEscalateApcAsRequired(
    Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {

    Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
	// 如果registry已经包含了AnnotationAwareAspectJAutoProxyCreator 
    // 则按照优先级顺序安排
    if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
        BeanDefinition apcDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
        if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
            int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
            int requiredPriority = findPriorityForClass(cls);
            if (currentPriority < requiredPriority) {
                apcDefinition.setBeanClassName(cls.getName());
            }
        }
        return null;
    }
	// 容器没有 AnnotationAwareAspectJAutoProxyCreator 则创建
    RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
    beanDefinition.setSource(source);
    beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
    beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
    return beanDefinition;
}
```



AnnotationAwareAspectJAutoProxyCreator 实现了 

SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor

重写了里面的 postProcessBeforeInstantiation 和 postProcessAfterInitialization 

在 doCreateBean 之前的 Object bean = resolveBeforeInstantiation(beanName, mbdToUse); 

调用 postProcessBeforeInstantiation  方法

initializeBean(beanName, exposedObject, mbd); 里面

调用 postProcessAfterInitialization  方法



**postProcessBeforeInstantiation  方法：获取增强**

isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)  方法判断当前类是否需要 被代理 

```java
this.advisedBeans.put(cacheKey, Boolean.FALSE);
```

shouldSkip 里面 缓存  切面类的名称

```java
// 这里缓存 实现 Aspect接口的
this.cachedAdvisorBeanNames = advisorNames
// 这里循环依赖的时候会缓存 this.earlyProxyReferences.put(cacheKey, bean);
// addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
this.beanFactory.getBean(name, Advisor.class)
// 这里处理 @Aspect 注解的    
this.aspectJAdvisorsBuilder.buildAspectJAdvisors()   
// 缓存名字
aspectNames = this.aspectBeanNames 
// 缓存--找到切面    
List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
if (this.beanFactory.isSingleton(beanName)) {
    this.advisorsCache.put(beanName, classAdvisors);
}

// Create proxy here if we have a custom TargetSource.
// Suppresses unnecessary default instantiation of the target bean:
// The TargetSource will handle target instances in a custom fashion.
// 当前 bean 实现 TargetSource接口的
// TargetSource 自定义 aop 的创建
TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
if (targetSource != null) {
    if (StringUtils.hasLength(beanName)) {
        this.targetSourcedBeans.add(beanName);
    }
    // 获取增强和切面
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
    // 创建代理
    Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
    this.proxyTypes.put(cacheKey, proxy.getClass());
    return proxy;
}
```

总结：缓存当前类是否需要 被 aop 代理--判断标准 （当前类是否是 切面，切入点，增强 这些类）

同时 shouldSkip 里面会缓存一些 容器里的切面



 **postProcessAfterInitialization方法 ：**

缓存：this.advisedBeans.put(cacheKey, Boolean.TRUE);

​	和    this.proxyTypes.put(cacheKey, proxy.getClass());

首先如果是 切面类 在 isInfrastructureClass(beanClass) 里面直接返回了

```java
/**
 * 如果bean被子类标识为要代理的bean，则使用配置的拦截器创建代理。
 * Create a proxy with the configured interceptors if the bean is
 * identified as one to proxy by the subclass.
 * @see #getAdvicesAndAdvisorsForBean
 */
@Override
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
    if (bean != null) {
        // 为beanName和beanClass构建缓存key
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        // 这个好像不太会用：切面类里面一般不会有循环依赖
        if (!this.earlyProxyReferences.contains(cacheKey)) {
            // 如果它适合被代理，则需要封装指定 bean 
            return wrapIfNecessary(bean, beanName, cacheKey);
        }
    }
    return bean;
}
```

 wrapIfNecessary方法 ：

```java
/**
 * 如果需要则包装该bean,例如该bean可以被代理
 * Wrap the given bean if necessary, i.e. if it is eligible for being proxied.
 * @param bean the raw bean instance
 * @param beanName the name of the bean
 * @param cacheKey the cache key for metadata access
 * @return a proxy wrapping the bean, or the raw bean instance as-is
 */
protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    // 1、如果已经处理过或者不需要创建代理，则返回
    // TargetSource接口已经处理过的不需要再次处理 
    if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    // 当前类不需要增强
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }
    // 给定的 bean 类是否代表一个基础设施类， 基础设施类不应代理，
    // 或者配置了指定 bean 不需要自动代理  
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // 2、创建代理
    // 2.1 根据指定的bean获取所有的适合该bean的增强
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    if (specificInterceptors != DO_NOT_PROXY) {
        // 2.2 有增强需要为指定bean创建代理
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        // 创建代理
        Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        // 缓存代理
        this.proxyTypes.put(cacheKey, proxy.getClass());
        return proxy;
    }

    // 3、没有增强缓存当前了不需要被代理
    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    return bean;
}
```

![](img/20191020214016.png)