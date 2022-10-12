package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Menu;
import com.chiangt.domain.vo.MenuTreeVo;
import com.chiangt.domain.vo.MenuVo;
import com.chiangt.service.MenuService;
import com.chiangt.utils.BeanCopyUtils;
import com.chiangt.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        List<Menu> menuList = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options = SystemConverter.buildMenuSelectTree(menuList);
        return ResponseResult.okResult(options);
    }

    @GetMapping("/list")
    public ResponseResult list(Menu menu){
        List<Menu> menuList = menuService.selectMenuList(menu);
        List<MenuVo> menuVoList = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);
        return ResponseResult.okResult(menuVoList);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        return ResponseResult.okResult(menuService.getById(id));
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu){
        if(menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(500, "修改菜单" + menu.getMenuName() + "失败，上级菜单不能选择当前菜单");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        if(menuService.hasChild(id)){
            return ResponseResult.errorResult(500, "删除失败，存在子菜单");
        }
        menuService.removeById(id);
        return ResponseResult.okResult();
    }
}
