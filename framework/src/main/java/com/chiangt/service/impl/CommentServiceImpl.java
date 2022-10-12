package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Comment;
import com.chiangt.domain.vo.CommentVo;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.enums.AppHttpCodeEnum;
import com.chiangt.exception.SystemException;
import com.chiangt.mapper.CommentMapper;
import com.chiangt.service.CommentService;
import com.chiangt.service.UserService;
import com.chiangt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-10-05 14:29:56
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize) {

        //查询某文章的根评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getType, type);
        lambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type), Comment::getArticleId, articleId);
        lambdaQueryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT_ID);
        //分页
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        List<CommentVo> commentVoList = toCommentVo(page.getRecords());
        //查询某文章的所有子评论
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> toCommentVo(List<Comment> list){
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //添加toCommentUserName和username两个字段
        for (CommentVo commentVo : commentVoList) {
            commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName());
            if(commentVo.getToCommentUserId() != -1){
                commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getNickName());
            }
        }
        return commentVoList;
    }

    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> childrenList = list(queryWrapper);
        return toCommentVo(childrenList);
    }
}

