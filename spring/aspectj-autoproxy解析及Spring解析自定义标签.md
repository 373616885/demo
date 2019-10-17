### aspectj-autoproxy 标签

<aop:aspectj-autoproxy  /> 而该标签有两个属性，proxy-target-class 和 expose-proxy。

 proxy-target-class : 默认false   如果被代理的目标对象至少实现了一个接口，则会使用JDK动态代理，所有实现该目标类实现的接口都将被代理；如果该目标对象没有实现任何接口，则创建CGLIB动态代理 