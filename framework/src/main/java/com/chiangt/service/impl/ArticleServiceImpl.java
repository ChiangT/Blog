package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.dto.AddArticleDto;
import com.chiangt.domain.entity.Article;
import com.chiangt.domain.entity.ArticleTag;
import com.chiangt.domain.entity.Category;
import com.chiangt.domain.vo.*;
import com.chiangt.mapper.ArticleMapper;
import com.chiangt.service.ArticleService;
import com.chiangt.service.ArticleTagService;
import com.chiangt.service.CategoryService;
import com.chiangt.utils.BeanCopyUtils;
import com.chiangt.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装为ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //正式文章、按照浏览量降序、最多的十篇
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1,10);
        page(page, queryWrapper);
        List<Article> articleList = page.getRecords();
        //bean拷贝
        List<HotArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, HotArticleVo.class);

        return ResponseResult.okResult(articleVoList);
    }

    /*
        分页查询首页和分类页面的文章
        文章为正式发布
        置顶文章显示在最前
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0, Article::getCategoryId, categoryId);
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        List<Article> articleList = page.getRecords();
        articleList = articleList.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category != null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(article.getTitle()), Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()), Article::getSummary, article.getSummary());
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<Article> articleList = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setRows(articleList);
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    @Override
    public ArticleVo getInfo(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        List<Long> tagId = articleTagList.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setTagId(tagId);
        return articleVo;
    }

    @Override
    public void edit(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        updateById(article);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);
        List<ArticleTag> articleTagList = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(addArticleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTagList);
    }


}
