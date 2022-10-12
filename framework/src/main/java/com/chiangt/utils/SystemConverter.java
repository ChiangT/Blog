package com.chiangt.utils;

import com.chiangt.domain.entity.Menu;
import com.chiangt.domain.vo.MenuTreeVo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemConverter {

    public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menuList){
        List<MenuTreeVo> menuTreeVoList = menuList.stream()
                .map(m -> new MenuTreeVo(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVo> options = menuTreeVoList.stream()
                .filter(o -> o.getParentId().equals(0L))
                .map(o -> o.setChildren(getChildrenList(menuTreeVoList, o)))
                .collect(Collectors.toList());
        return options;
    }

    private static List<MenuTreeVo> getChildrenList(List<MenuTreeVo> menuTreeVoList, MenuTreeVo parent) {
        List<MenuTreeVo> options = menuTreeVoList.stream()
                .filter(o -> Objects.equals(o.getParentId(), parent.getId()))
                .map(o -> o.setChildren(getChildrenList(menuTreeVoList, o)))
                .collect(Collectors.toList());
        return options;
    }
}
