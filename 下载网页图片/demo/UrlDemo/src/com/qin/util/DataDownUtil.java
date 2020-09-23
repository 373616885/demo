package com.qin.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 下载网页的东西
 * @author Administrator
 */

public class DataDownUtil {
	/**
	 * 获取页面的源代码
	 * @param url 网页路径
	 * @param endcoding 编码格式
	 * @return html 源码<br />  qin
	 */
	public static String getHtmlResourceByUrl(String url, String endcoding) {
		//存放html源码
		StringBuffer sb = new StringBuffer();
		URL urlObj = null;
		URLConnection uc = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			// 建立网络连接
			urlObj = new URL(url);
			// 打开连接
			uc = urlObj.openConnection();
			// 建立文件写入流
			isr = new InputStreamReader(uc.getInputStream(), endcoding);
			// 建立缓冲流
			reader = new BufferedReader(isr);
			// 建立临时文件
			String temp = null;
			// 一行一行读
			while ((temp = reader.readLine()) != null) {
				sb.append(temp + "\n");// 追加到源码
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("------网络连接失败-----");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("------IO读取失败-----");
		} finally {
			try {
				isr.close();
			} catch (IOException e) {
				System.out.println("文件流关闭失败");
			}
		}

		return sb.toString();

	}

	/**
	 * 图片保存到本地
	 * @param ImgUrl
	 */
	public static void downImge(String imgUrl, String fileName) {
		try {
			// 创建流
			BufferedInputStream in = new BufferedInputStream(new URL(imgUrl).openStream());
			File imgFile = new File(fileName);
			// 生成图片
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imgFile));
			byte[] buf = new byte[2048];
			int length = in.read(buf);
			while (length != -1) {
				out.write(buf, 0, length);
				length = in.read(buf);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * main方法测试
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://sj.zol.com.cn/bizhi/meinv/1080x1920/";
		String endcoding = "GBK";

		// 根据网页和编码格式获取源代码
		String html = getHtmlResourceByUrl(url, endcoding);

		//System.out.println(html);

		Document doc = Jsoup.parse(html);
		Elements imgEles = doc.getElementsByTag("img");
		System.out.println("------down picture Start-----");
		int imgeSize = imgEles.size();
		for (int i = 0; i<imgeSize; i++) {
			Element imgEle = imgEles.get(i);
			String imgUrl = imgEle.attr("src");
			if(null == imgUrl||"".equals(imgUrl)){
				imgUrl =imgEle.attr("srch");
			}
			// <img >标签 src 属性  http：// 开始才可以下载
			if (imgUrl.indexOf("http://") != -1) {
				// 存放地址
				String fileName = "img" + i + "." + imgUrl.substring(imgUrl.lastIndexOf("."), imgUrl.length());
				downImge(imgUrl, fileName);
			}

		}
		System.out.println("------down picture End-----");

	}

}
