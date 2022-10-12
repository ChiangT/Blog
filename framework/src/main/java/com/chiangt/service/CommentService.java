package com.chiangt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-10-05 14:29:56
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

}

