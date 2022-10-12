package com.chiangt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chiangt.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-04 14:24:41
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
