# 凌凯短信模块



# 代码文件结构  
  ├domian
  │  │
  │  └LingkaiSmsProperty  配置文件 sms.properties 属性类
  ├service
  │  │
  │  └LingkaiSmsService   凌凯短信业务类 
  ├util
  │  │
  │  └RandomNumUtil   随机数类 
  │  │
  ├LingkaiSmsCinfig   引人的短信模块的配置类
  │
  └resources
  │  │
  │  └sms.properties   凌凯的配置信息 



#  主要类函数说明

**LingkaiSmsService.sendMobileCaptcha( String mobile )**

功能：发送短信验证码
参数：手机号码
描述：返回一个数字字符串 -- 这个数字大于零则代表发送成功



# 代码示例

1. 注入配置类LingkaiSmsCinfig.class  -- （@Import(LingkaiSmsCinfig.class)）

```java
@Import(LingkaiSmsCinfig.class)
@SpringBootApplication
public class WmaApplication {
    public static void main(String[] args) {
        SpringApplication.run(WmaApplication.class, args);
    }
}
```

2. 业务层使用 -- 注入 LingkaiSmsService 使用 sendMobileCaptcha 方法

```java
@Autowired
LingkaiSmsService service;
// 发送短信 -- 返回值大于零则代表成功
String date = service.sendMobileCaptcha(mobile);

```


