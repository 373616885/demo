package com.qin.jasypt;

import org.jasypt.util.text.AES256TextEncryptor;

public class PasswordGenerate {

    public static void main(String[] args) {

        /*
         * BasicTextEncryptor 对应的加密模式：PBEWithMD5AndDES
         * StrongTextEncryptor 对应的加密模式：PBEWithMD5AndTripleDES
         * AES256TextEncryptor 对应的加密模式：PBEWithHMACSHA512AndAES_256
         *
         * 为了防止salt(盐)泄露,反解出密码.可以在项目部署的时候使用命令传入salt(盐)值
         * java -jar -Djasypt.encryptor.password=78802581 xxx.jar
         * 或者在服务器的环境变量里配置,进一步提高安全性
         *
         * 打开/etc/profile文件
         * vim /etc/profile
         *
         * 文件末尾插入
         * export JASYPT_PASSWORD = 78802581
         *
         * 编译
         * source /etc/profile
         *
         * 运行
         * java -jar -Djasypt.encryptor.password=${JASYPT_PASSWORD} xxx.jar
         *
         */

        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("78802581");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("373616885");
        System.out.println("username:" + username);
        System.out.println("password:" + password);

        //解密方法
        System.out.println(textEncryptor.decrypt("ak9Ul9wQ+syTwMgv4FWroDbnJZ0N40jZ7JmhXLCJ/+7uR+ba4RIrXfCjURPCoV+g"));


    }
}
