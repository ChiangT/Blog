package com.chiangt.utils;

import com.chiangt.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    //获取当前登录用户
    public static LoginUser getLoginUser(){
        return (LoginUser) getAuthentication().getPrincipal();
    }

    //获取当前认证Authentication
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getUserId(){
        return getLoginUser().getUser().getId();
    }

    public static Boolean isAdmin(){
        Long id = getUserId();
        return id != null && id.equals(1L);
    }
}
