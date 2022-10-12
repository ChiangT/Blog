package com.chiangt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chiangt.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-08 12:04:33
 */

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
