package com.example.demo.mapper;

import com.example.demo.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //SELECT * FROM article;
    @Select("SELECT * FROM article;")
    List<Article> selectAll();

    //SELECT * FROM article WHERE id;
    @Select("SELECT * FROM article WHERE id=#{id}")
    Article selectById(@Param("id") Long id);
}
