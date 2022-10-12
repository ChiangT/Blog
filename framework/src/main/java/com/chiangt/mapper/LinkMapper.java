package com.chiangt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chiangt.domain.entity.Link;
import org.apache.ibatis.annotations.Mapper;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-04 12:44:15
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}
