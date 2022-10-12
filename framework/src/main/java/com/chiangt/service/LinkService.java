package com.chiangt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Link;
import com.chiangt.domain.vo.PageVo;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 12:44:15
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVo selectLinkPage(Integer pageNum, Integer pageSize, Link link);
}

