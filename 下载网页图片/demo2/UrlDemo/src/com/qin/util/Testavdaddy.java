package com.qin.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Testavdaddy {
	public static void main(String[] args) {
		//for(int j=4;j<=8;j++){
			String html =DataDownUtil.getHtmlResourceByPostUrl("http://www.avdaddy.com/index.php?option=com_content&view=article&id=12&Itemid=112", "utf-8","page_num=5");
			Document doc = Jsoup.parse(html);
			Elements eles = doc.getElementsByTag("img");
			int elesSize = eles.size();
			System.out.println(elesSize);
			for (int i = 0; i<elesSize; i++) {
				Element imgEle = eles.get(i);
				String imgUrl = imgEle.attr("src");
				String fileName =  imgUrl.substring(imgUrl.lastIndexOf("/")+1);
				imgUrl=  imgUrl.replace("_c", "_p");
				imgUrl=imgUrl.replace("c.jpg", ".jpg");
				imgUrl ="http://avdaddy.com"+imgUrl;
				DataDownUtil.downImge(imgUrl, fileName);
			}
		//}
		
	}

}
