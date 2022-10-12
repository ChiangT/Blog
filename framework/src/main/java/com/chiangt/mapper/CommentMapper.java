package com.chiangt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chiangt.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-05 14:29:56
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
