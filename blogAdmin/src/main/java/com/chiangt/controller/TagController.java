package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.dto.AddTagDto;
import com.chiangt.domain.dto.EditTagDto;
import com.chiangt.domain.dto.TagListDto;
import com.chiangt.domain.entity.Tag;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.domain.vo.TagVo;
import com.chiangt.service.TagService;
import com.chiangt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> tagList(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    @PutMapping
    public ResponseResult edit(@RequestBody EditTagDto editTagDto){
        Tag tag = BeanCopyUtils.copyBean(editTagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> tagVoList = tagService.listAllTag();
        return ResponseResult.okResult(tagVoList);
    }
}
