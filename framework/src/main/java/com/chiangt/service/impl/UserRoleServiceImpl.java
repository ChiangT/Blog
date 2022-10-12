package com.chiangt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.domain.entity.UserRole;
import com.chiangt.mapper.UserRoleMapper;
import com.chiangt.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-10-11 20:25:15
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

