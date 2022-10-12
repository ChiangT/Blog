package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.dto.AddArticleDto;
import com.chiangt.domain.entity.Article;
import com.chiangt.domain.vo.ArticleVo;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){
        return articleService.add(addArticleDto);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, Article article){
        PageVo pageVo = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleVo articleVo = articleService.getInfo(id);
        return ResponseResult.okResult(articleVo);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody AddArticleDto addArticleDto){
        articleService.edit(addArticleDto);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
