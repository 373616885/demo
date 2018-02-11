package com.qin.spring.demo.mybatis.client;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.qin.spring.demo.mybatis.model.Article;
import com.qin.spring.demo.mybatis.model.ArticleExample;

public interface ArticleMapper {

    long countByExample(ArticleExample example);

    int deleteByExample(ArticleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    List<Article> selectByExample(ArticleExample example);

    Article selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByExample(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    @Select("select * from t_article")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "title",  column = "title"),
            @Result(property = "quote", column = "quote"),
            @Result(property = "content", column = "content")
    })
    List<Article> getAll();
}
