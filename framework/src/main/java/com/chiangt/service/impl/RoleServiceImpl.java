package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.entity.Role;
import com.chiangt.domain.entity.RoleMenu;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.mapper.RoleMapper;
import com.chiangt.service.RoleMenuService;
import com.chiangt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 15:33:30
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //管理员返回admin，否则查询用户所具有的角色信息
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public PageVo selectRoleList(Integer pageNum, Integer pageSize, Role role) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName());
        queryWrapper.like(StringUtils.hasText(role.getStatus()), Role::getStatus, role.getStatus());
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<Role> roleList = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setRows(roleList);
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    @Override
    public void insertRole(Role role) {
        save(role);
        if(role.getMenuIds() != null && role.getMenuIds().length > 0){
            insertRoleMenu(role);
        }
    }

    @Override
    public List<Role> selectAllRole() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL));
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long id) {
        return getBaseMapper().selectRoleIdByUserId(id);
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}

