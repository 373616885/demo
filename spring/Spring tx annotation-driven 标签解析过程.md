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





























