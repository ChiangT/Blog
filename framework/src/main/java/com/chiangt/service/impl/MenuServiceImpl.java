package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.entity.Menu;
import com.chiangt.mapper.MenuMapper;
import com.chiangt.service.MenuService;
import com.chiangt.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 15:25:39
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //根据用户ID查询权限（管理员返回所有权限，普通用户返回相应权限）
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menuList = list(queryWrapper);
            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        //如果是管理员则返回所有符合要求的menu，否则返回当前用户所拥有的menu
        MenuMapper mapper = getBaseMapper();
        List<Menu> menuList = null;
        if(SecurityUtils.isAdmin()){
            menuList = mapper.selectAllRouterMenu();
        } else {
            menuList = mapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        return buildMenuTree(menuList, 0L);
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        queryWrapper.like(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menuList = list(queryWrapper);
        return menuList;
    }

    @Override
    public boolean hasChild(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        return count(queryWrapper) != 0;
    }

    private List<Menu> buildMenuTree(List<Menu> menuList, Long parentId) {
        List<Menu> menuTree = menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menuList)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menuList) {
        List<Menu> childrenList = menuList.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menuList)))
                .collect(Collectors.toList());
        return childrenList;
    }


}

