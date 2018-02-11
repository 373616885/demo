package com.qin.spring.demo;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qin.spring.demo.mybatis.client.ArticleMapper;
import com.qin.spring.demo.mybatis.model.Article;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
	
	@Autowired
	private ArticleMapper articleMapper;

	@Test
	public void contextLoads() {
		List<Article> articles = articleMapper.getAll();
		
		for(Article article:articles) {
			System.out.println("ID:"+article.getId());
			System.out.println("title:"+article.getTitle());
			System.out.println("quote:"+article.getQuote());
			System.out.println("content:"+article.getContent());
		}
		
		Article article = articleMapper.selectByPrimaryKey(1);
		System.out.println("ID:"+article.getId());
		System.out.println("title:"+article.getTitle());
		System.out.println("quote:"+article.getQuote());
		System.out.println("content:"+article.getContent());
		
		

	}

}
