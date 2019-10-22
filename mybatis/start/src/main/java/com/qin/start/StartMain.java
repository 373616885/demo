package com.qin.start;

import com.qin.start.bean.MyTestBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class StartMain {

    public static void main(String[] args) throws IOException {
        //WApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));

        MyTestBean bean=(MyTestBean) bf.getBean("myTestBean");
        System.out.println(bean.getTestStr());

        Resource resource= new ClassPathResource("beanFactoryTest.xml");
        InputStream inputStream = resource.getInputStream();
    }
}
