### Spring tx:annotation-driven 标签解析过程

##### 快速定位Spring自定义标签解析入口：

配置文件中，通过 <tx:annotation-driven transaction-manager="transactionManager"/> 开启了注解事物

以此为入口 , tx是Spring的自定义标签，而Spring通过继承NamespaceHandler自定义命名空间的解析   

根据之前的经验 全局搜索 **annotation-driven**  可以找到TxNamespaceHandler类下包含了`annotation-driven` 

```java
public class TxNamespaceHandler extends NamespaceHandlerSupport {

	static final String TRANSACTION_MANAGER_ATTRIBUTE = "transaction-manager";

	static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
	// transaction-manager 属性的默认值是 transactionManager
	static String getTransactionManagerName(Element element) {
		return (element.hasAttribute(TRANSACTION_MANAGER_ATTRIBUTE) ?
				element.getAttribute(TRANSACTION_MANAGER_ATTRIBUTE) : DEFAULT_TRANSACTION_MANAGER_BEAN_NAME);
	}

	@Override
	public void init() {
        // 注册 TxAdviceBeanDefinitionParser 事务增强类
		registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
        // 注册 AnnotationDrivenBeanDefinitionParser
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
		registerBeanDefinitionParser("jta-transaction-manager", new JtaTransactionManagerBeanDefinitionParser());
	}

}
```



AnnotationDrivenBeanDefinitionParser 

```java
@Override
	@Nullable
public BeanDefinition parse(Element element, ParserContext parserContext) {
    // 注册 TransactionalEventListenerFactory
    registerTransactionalEventListenerFactory(parserContext);
    // <tx:annotation-driven transaction-manager="transactionManager" mode="aspectj"/>
    // 解析标签的mode属性 默认 proxy 模式
    String mode = element.getAttribute("mode");
   
    if ("aspectj".equals(mode)) {
        // mode="aspectj"
        registerTransactionAspect(element, parserContext);
        if (ClassUtils.isPresent("javax.transaction.Transactional", getClass().getClassLoader())) {
            registerJtaTransactionAspect(element, parserContext);
        }
    }
    else {
        // mode="proxy"
        // 默认
        AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
    }
    return null;
}
```



 proxy模式 ：

```java
private static class AopAutoProxyConfigurer {

		public static void configureAutoProxyCreator(Element element, ParserContext parserContext) {
            // 1、注册 InfrastructureAdvisorAutoProxyCreator 这个内部类
            // 名称：AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME = 
            //           "org.springframework.aop.config.internalAutoProxyCreator";
			AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
			// 切面类名称
			String txAdvisorBeanName = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME;
            
			if (!parserContext.getRegistry().containsBeanDefinition(txAdvisorBeanName)) {
				Object eleSource = parserContext.extractSource(element);
                
				// 2、创建AnnotationTransactionAttributeSource的BeanDefinition
				// Create the TransactionAttributeSource definition.
				RootBeanDefinition sourceDef = new RootBeanDefinition(
						"org.springframework.transaction.annotation.AnnotationTransactionAttributeSource");
                
				sourceDef.setSource(eleSource);
                // 这个bean属于内部类
				sourceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                // 注册bean ，并使用 spring 内部自定义的规则 类名 + # + 数字 0 开始  
				String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);

				// Create the TransactionInterceptor definition.
                // 3、创建 TransactionInterceptor 这个 bean （执行里面的 invoke 方法）
				RootBeanDefinition interceptorDef = new RootBeanDefinition(TransactionInterceptor.class);
				interceptorDef.setSource(eleSource);
                // 内部类
				interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                // 解析 transaction-manager 属性 并注册 默认 transactionManager
				registerTransactionManager(element, interceptorDef);
                // 添加 transactionAttributeSource 属性
                // 里面的属性都是不可变的
				interceptorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
                
                // 注册bean ，并使用 spring 内部自定义的规则 类名 + # + 数字 0 开始  
				String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);

				// Create the TransactionAttributeSourceAdvisor definition.
                // 4、创建BeanFactoryTransactionAttributeSourceAdvisor的BeanDefinition（切面）
				RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryTransactionAttributeSourceAdvisor.class);
				advisorDef.setSource(eleSource);
                // spring内部类
				advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                // 添加 transactionAttributeSource 属性
                // 里面的属性都是不可变的
				advisorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
                // 添加属性 adviceBeanName 属性 上面的：TransactionInterceptor
				advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
				if (element.hasAttribute("order")) {
					advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
				}
                // 注册切面：TRANSACTION_ADVISOR_BEAN_NAME 
				parserContext.getRegistry().registerBeanDefinition(txAdvisorBeanName, advisorDef);
				
                /**
                 * 通过上面的操作：
                 * 1、创建AnnotationTransactionAttributeSource的BeanDefinition
                 * 2、创建TransactionInterceptor的BeanDefinition
                 * 3、创建BeanFactoryTransactionAttributeSourceAdvisor的BeanDefinition
                 */
                
				CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
				compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
				compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
				compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, txAdvisorBeanName));
				parserContext.registerComponent(compositeDef);
			}
		}
	}

public static void registerAutoProxyCreatorIfNecessary(
			ParserContext parserContext, Element sourceElement) {
	// 注册 InfrastructureAdvisorAutoProxyCreator 这个内部类
    BeanDefinition beanDefinition = AopConfigUtils.registerAutoProxyCreatorIfNecessary(
        parserContext.getRegistry(), parserContext.extractSource(sourceElement));
    // 解析标签的 proxy-target-class 属性 ，
    // 注意：这里 <tx:annotation-driven  /> 这个元素没有 expose-proxy 这个属性
    useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
    registerComponentIfNecessary(beanDefinition, parserContext);
}
```

上面注册了 3个bean ,这3个bean支撑了spring 的整个事务体系

组装的关系：

advisorDef 使用 BeanFactoryTransactionAttributeSourceAdvisor 作为其 class 属性

transactionAttributeSource 属性 等于：AnnotationTransactionAttributeSource

adviceBeanName属性 等于：TransactionInterceptor

![](img/20191103225201.png)

开始的  注册 **InfrastructureAdvisorAutoProxyCreator** 这个类的继承关系

![](img/20191103225528.png)



![](img/20191103225629.png)



InfrastructureAdvisorAutoProxyCreator 继承  AbstractAdvisorAutoProxyCreator 

AbstractAdvisorAutoProxyCreator  继承 AbstractAutoProxyCreator

AbstractAutoProxyCreator 实现了 SmartInstantiationAwareBeanPostProcessor 接口

SmartlnstantiationAwareBeanPostProcessm 又间接继承了 InstantiationAwareBeanPostProcessor

而 AbstractAutoProxyCreator  实现了里面的 postProcessBeforeInstantiation 和 postProcessAfterInitialization 

在所有 bean 实例化时 Spring 都会调用其AbstractAutoProxyCreator  类的 postProcessBeforeInstantiation和 postProcessAfterInitialization 方法 。 



SpringAOP时，曾经注册了AnnotationAwareAspectJAutoProxyCreator,该类的作用是为目标对象自动创建代理，该类也是间接实现了BeanPostProcessor接口，所以会在 所有的bean实例化前后调用postProcessBeforeInstantiation和postProcessAfterInitialization方法



**AnnotationTransactionAttributeSource的BeanDefinition** ：

![](img/20191103231824.png)



从类的继承结构。可以看到该类实现了TransactionAttributeSource接口，该接口只提供了一个获取TransactionAttribute的方法TransactionAttribute getTransactionAttribute(Method method, @Nullable Class<?> targetClass);。TransactionAttribute实现了TransactionDefinition接口，该接口提供了Spring事物的传播特性以及事物隔离级别等定义

综合上述分析，大致可以了解到AnnotationTransactionAttributeSource可以用来获取类、接口、方法上的事物注解属性 



**TransactionInterceptor的BeanDefinition** 

![](img/20191103232211.png)

 TransactionInterceptor是Spring声明式事物管理器的基础 

 查看其类图结构，TransactionInterceptor继承了TransactionAspectSupport并实现了MethodInterceptor接口 

 TransactionAspectSupport提供了对事物支持的能力 

 MethodInterceptor我们之前已经介绍过，可以用来实现环绕增强 

 从此我们大致可以推断出，Spring的事物管理是基于环绕增强的。该类的具体方法以及调用过程留在后面分析。 



 **TransactionAttributeSourceAdvisor的BeanDefinition** 

 上面已经创建了事物属性定义、事物增强定义、那么接下来就应该创建切面了 

![](img/20191103232510.png)

 从类图上看到BeanFactoryTransactionAttributeSourceAdvisor是PointcutAdvisor类型的切面 

 创建了该bean的定义之后，又将上面创建的AnnotationTransactionAttributeSource注入到transactionAttributeSource属性中 

 将TransactionInterceptor注入到adviceBeanName属性中 





















