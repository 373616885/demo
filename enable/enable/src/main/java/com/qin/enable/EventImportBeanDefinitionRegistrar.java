package com.qin.enable;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class EventImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean d = registry.containsBeanDefinition("event");
        if (!d) {
            //BeanDefinition beanDefinition = new GenericBeanDefinition();
            BeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClassName(Event.class.getName());
            beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
            MutablePropertyValues values = beanDefinition.getPropertyValues();
            values.addPropertyValue("id", 1);
            values.addPropertyValue("name", "qinjp");
            //这里注册bean
            registry.registerBeanDefinition("event", beanDefinition );
        }

    }
}
