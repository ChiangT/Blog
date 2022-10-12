package com.chiangt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.domain.entity.RoleMenu;
import com.chiangt.mapper.RoleMenuMapper;
import com.chiangt.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-11 16:18:17
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

