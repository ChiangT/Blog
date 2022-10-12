package com.chiangt.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Category;
import com.chiangt.domain.vo.CategoryVo;
import com.chiangt.domain.vo.ExcelCategoryVo;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.enums.AppHttpCodeEnum;
import com.chiangt.service.CategoryService;
import com.chiangt.utils.BeanCopyUtils;
import com.chiangt.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, Category category){
        PageVo pageVo = categoryService.selectCategoryPage(pageNum, pageSize, category);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Category category){
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            List<Category> categoryList = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVoList = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出").doWrite(excelCategoryVoList);
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
