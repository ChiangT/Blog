package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Article;
import com.chiangt.domain.entity.Category;
import com.chiangt.domain.vo.CategoryVo;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.mapper.CategoryMapper;
import com.chiangt.service.ArticleService;
import com.chiangt.service.CategoryService;
import com.chiangt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 15:19:04
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    /*
        只展示含有发布文章的分类
        分类状态为正常
     */
    @Override
    public ResponseResult getCategoryList() {

        //查询文章表中状态为已发布的文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);

        //获取文章分类id并去重
        Set<Long> categoryId = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categoryList = listByIds(categoryId);
        categoryList = categoryList.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装vo
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);

        return ResponseResult.okResult(categoryVoList);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categoryList = list(queryWrapper);
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return categoryVoList;
    }

    @Override
    public PageVo selectCategoryPage(Integer pageNum, Integer pageSize, Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(category.getName()), Category::getName, category.getName());
        queryWrapper.like(StringUtils.hasText(category.getStatus()), Category::getStatus, category.getStatus());
        Page<Category> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page(page, queryWrapper);
        List<Category> categoryList = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setRows(categoryList);
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }
}

