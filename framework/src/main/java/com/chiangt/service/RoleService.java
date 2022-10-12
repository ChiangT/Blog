package com.chiangt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chiangt.domain.entity.Role;
import com.chiangt.domain.vo.PageVo;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 15:33:30
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    PageVo selectRoleList(Integer pageNum, Integer pageSize, Role role);

    void insertRole(Role role);

    List<Role> selectAllRole();

    List<Long> selectRoleIdByUserId(Long id);
}

