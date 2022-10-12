package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Link;
import com.chiangt.domain.vo.LinkVo;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.mapper.LinkMapper;
import com.chiangt.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import com.chiangt.service.LinkService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-10-04 12:44:15
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(linkLambdaQueryWrapper);
        List<LinkVo> linkVoList = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVoList);
    }

    @Override
    public PageVo selectLinkPage(Integer pageNum, Integer pageSize, Link link) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(link.getName()), Link::getName, link.getName());
        queryWrapper.like(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus());
        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<Link> linkList = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setRows(linkList);
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }
}

