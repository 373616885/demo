### Spring 源码日志

​		XmlBeanFactory 继承于 DefaultListableBeanFactory 而 DefaultListableBeanFactmy 是整个 bean 加载的核心部分，是 Spring 注册及加载 bean 的默认实现 ，一个比较全面的对象工厂。

​		XmlBeanFactory  对 DefaultListableBeanFactory 类进行了扩展，主要用于从 XML 文档中读取BeanDefinition，对于注册及获取 bean 都是使用从父类 DefaultListableBeanFactory 继承的方 法去实现

​		XmlBeanFactory  与 DefaultListableBeanFactory  不同就是增加了 XmlBeanDefinitionReader 类型的 reader 属性。 在 XmlBeanFactory   中主要使用 reader 属性对资源文件进行读取和注册

​		



1. XmlBeanFactory 首先创建对象 （继承了DefaultListableBeanFactory   ）

2. XmlBeanDefinitionReader.loadBeanDefinitions(resource); 加载 XML 

3. Document doc = doLoadDocument(inputSource, resource); 获取 doc

4. registerBeanDefinitions(Document doc, XmlReaderContext readerContext) ; 解析并注册BeanDefinition

5. parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) ; 解析 doc

6. parseBeanDefinitionElement(ele, null);  得到 BeanDefinitionHolder

7. BeanDefinitionHolder 里面有属性 beanDefinition, beanName,String[] aliases

8. AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);默认使用 GenericBeanDefinition 保存 bean 的信息

9. 生成的  GenericBeanDefinition  保存到 BeanDefinitionHolder  里面

10. BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());注册beanDefinition 的信息到 XmlBeanFactory 里面

11. DefaultListableBeanFactory.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition()); 注册 到 beanDefinitionMap ，beanDefinitionNames 里面

    
