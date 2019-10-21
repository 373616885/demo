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



### 获取增强器



```java
@Override
@Nullable
protected Object[] getAdvicesAndAdvisorsForBean(
    Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
	// 寻找具备条件的增强器
    List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
    // 找不到对应的增强器
    if (advisors.isEmpty()) {
        // 返回空
        return DO_NOT_PROXY;
    }
    // 返回对应的增强器
    return advisors.toArray();
}

protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
    // 这里查找所有的切面 :
    //      Advisor 这个接口的实现类
    //      从当前BeanFactory中查找所有标记了@AspectJ的注解的bean
	List<Advisor> candidateAdvisors = findCandidateAdvisors();
    
    List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
    
    extendAdvisors(eligibleAdvisors);
    
    if (!eligibleAdvisors.isEmpty()) {
        eligibleAdvisors = sortAdvisors(eligibleAdvisors);
    }
    return eligibleAdvisors;
}


@Override
protected List<Advisor> findCandidateAdvisors() {
    // Add all the Spring advisors found according to superclass rules.
    // 当使用注解方式配置 AOP 的时候并不是丢弃了对 XML 配置的支持， 
    // 这里查找所有的切面 :
    //		Advisor 这个接口的实现类
    List<Advisor> advisors = super.findCandidateAdvisors();
    // Build Advisors for all AspectJ aspects in the bean factory.
    if (this.aspectJAdvisorsBuilder != null) {
        // 从当前BeanFactory中查找所有标记了@AspectJ的注解的bean，并返回增强注解集合
        advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
    }
    return advisors;
}
```



```java
/**
 * Look for AspectJ-annotated aspect beans in the current bean factory,
 * and return to a list of Spring AOP Advisors representing them.
 * <p>Creates a Spring Advisor for each AspectJ advice method.
 * @return the list of {@link org.springframework.aop.Advisor} beans
 * @see #isEligibleBean
 */
// 查找所有 AspectJ-annotated 的 aspect beans
public List<Advisor> buildAspectJAdvisors() {
    // 在实例化之前已经找过一遍   
    // ps:postProcessBeforeInstantiation里面的shouldSkip() 
    // shouldSkip() 会缓存 aspectBeanNames 
    List<String> aspectNames = this.aspectBeanNames;

    if (aspectNames == null) {
        synchronized (this) {
            aspectNames = this.aspectBeanNames;
            if (aspectNames == null) {
                
                List<Advisor> advisors = new ArrayList<>();
                aspectNames = new ArrayList<>();
                
                // 获取所有的 beanName 
                String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                    this.beanFactory, Object.class, true, false);
                
               // 循环遍历所有的 beanName 找出对应的增强方法 
                for (String beanName : beanNames) {
                    // 当前 beanName 是否在<aop:include name=""/> 里面 
                    if (!isEligibleBean(beanName)) {
                        continue;
                    }
                    
                    // We must be careful not to instantiate beans eagerly as in this case they
                    // would be cached by the Spring container but would not have been weaved.
                    
                    Class<?> beanType = this.beanFactory.getType(beanName);
                    if (beanType == null) {
                        continue;
                    }
                    
                    // 没有 @Aspect 和 名字 ajc$ 这个开头的 都是 aspect bean
                    if (this.advisorFactory.isAspect(beanType)) {
                        // 后面缓存名称
                        aspectNames.add(beanName);
                        // 封装 Aspect信息类
                        AspectMetadata amd = new AspectMetadata(beanType, beanName);
                        // 
                        if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
                            MetadataAwareAspectInstanceFactory factory =
                                new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
                            
                            // 解析标记 AspectJ 注解中的增强方法 
                            List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
/**
 * 切面实例化模型简介
 *
 * singleton: 即切面只会有一个实例；
 * perthis  : 每个切入点表达式匹配的连接点对应的AOP对象都会创建一个新切面实例；
 *            使用@Aspect("perthis(切入点表达式)")指定切入点表达式；
 *            例如: @Aspect("perthis(this(com.lyc.cn.v2.day04.aspectj.Dog))")
 * pertarget: 每个切入点表达式匹配的连接点对应的目标对象都会创建一个新的切面实例；
 *            使用@Aspect("pertarget(切入点表达式)")指定切入点表达式；
 *            例如:
 *
 * 默认是singleton实例化模型，Schema风格只支持singleton实例化模型，
 *  而@AspectJ风格支持这三种实例化模型。
 */
                        	// 切面 singleton实例化模型处理
                            if (this.beanFactory.isSingleton(beanName)) {
                                // 缓存对应的切面
                                this.advisorsCache.put(beanName, classAdvisors);
                            } else {
                                // 非单例缓存 factory 
                                this.aspectFactoryCache.put(beanName, factory);
                            }
                            advisors.addAll(classAdvisors);
                        }  else {
                            // Per target or per this.
                            // 切面 perthis或pertarget实例化模型处理
                            if (this.beanFactory.isSingleton(beanName)) {
                                throw new IllegalArgumentException("Bean with name '" + beanName +
                                                                   "' is a singleton, but aspect instantiation model is not singleton");
                            }
                            MetadataAwareAspectInstanceFactory factory =
                                new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
                            this.aspectFactoryCache.put(beanName, factory);
                            advisors.addAll(this.advisorFactory.getAdvisors(factory));
                        }
                    }
                }
                this.aspectBeanNames = aspectNames;
                return advisors;
            }
        }
    }

    if (aspectNames.isEmpty()) {
        return Collections.emptyList();
    }
    List<Advisor> advisors = new ArrayList<>();
    for (String aspectName : aspectNames) {
        List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
        if (cachedAdvisors != null) {
            advisors.addAll(cachedAdvisors);
        }
        else {
            MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
            advisors.addAll(this.advisorFactory.getAdvisors(factory));
        }
    }
    return advisors;
}
```

