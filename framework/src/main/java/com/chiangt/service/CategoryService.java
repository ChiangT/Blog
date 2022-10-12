package com.chiangt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Category;
import com.chiangt.domain.vo.CategoryVo;
import com.chiangt.domain.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-10-02 15:19:04
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    PageVo selectCategoryPage(Integer pageNum, Integer pageSize, Category category);
}

