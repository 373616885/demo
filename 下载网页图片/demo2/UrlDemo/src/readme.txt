
 ╭───────────────────╮
   ────┤           下载网页             ├────
 ╰───────────────────╯ 
　                                                                  
　     下载网页
         开发语言：JAVA
         版       权：练习1.0
　      制 作 者： qin
         联系方式：18376731303

    ─────────────────────────────────

───────
 代码文件结构
───────

UelDemo
  │
  ├src┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈类文件夹
  │  │
  │  └com.qin.util
  │       │
  │		  └DataDownUtil.java┈┈┈┈┈┈主要类
  │
  ├WebRoot
  │  │
  │  └WEB-INF
  │   	  │
  │       └lib   ┈┈┈┈┈┈（依赖包）主要解析html文件的
  │   	     │
  │   	     └jsoup-1.8.3.jar┈┈┈┈┈┈主要用于解析html的
  │
  └readme.txt ┈┈┈┈┈┈┈┈┈使用说明文本

※注意※
保存本地的地址是java允许的类路径
弄成web工程可以再tomcat启动的时候找错误信息

─────────
 类文件函数结构
─────────

DataDownUtil.java

public static String getHtmlResourceByUrl(String url, String endcoding) 
功能：获取页面的源代码
输入：url 网页的地址
输出：endcoding 网页的编码格式

public static void downImge(String imgUrl, String fileName) 
功能：图片保存到本地
输入：imgUrl 图片的地址
输出：fileName 存储本地的名字

public static void main(String[] args) 
功能：main方法测试

┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉


──────────
 练习写文档
──────────





