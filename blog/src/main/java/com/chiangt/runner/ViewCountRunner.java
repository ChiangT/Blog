package com.chiangt.runner;

import com.chiangt.domain.entity.Article;
import com.chiangt.mapper.ArticleMapper;
import com.chiangt.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询blog信息 id : viewCount 存储到redis中
        List<Article> articleList = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        redisCache.setCacheMap("article:viewCount", viewCountMap);
    }
}
