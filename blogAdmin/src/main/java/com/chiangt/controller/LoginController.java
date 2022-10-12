package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.LoginUser;
import com.chiangt.domain.entity.Menu;
import com.chiangt.domain.entity.User;
import com.chiangt.domain.vo.AdminUserInfoVo;
import com.chiangt.domain.vo.RoutersVo;
import com.chiangt.domain.vo.UserInfoVo;
import com.chiangt.enums.AppHttpCodeEnum;
import com.chiangt.exception.SystemException;
import com.chiangt.service.LoginService;
import com.chiangt.service.MenuService;
import com.chiangt.service.RoleService;
import com.chiangt.utils.BeanCopyUtils;
import com.chiangt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //查询相应的权限信息和角色信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //封装数据
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu
        List<Menu> menuList = menuService.selectRouterMenuTreeByUserId(userId);
        return ResponseResult.okResult(new RoutersVo(menuList));
    }


}
