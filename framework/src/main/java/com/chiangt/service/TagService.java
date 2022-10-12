package com.chiangt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.dto.TagListDto;
import com.chiangt.domain.entity.Tag;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 12:04:32
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    List<TagVo> listAllTag();
}

