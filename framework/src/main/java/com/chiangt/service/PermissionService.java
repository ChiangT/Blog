package com.chiangt.service;

import com.chiangt.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /*
        判断当前用户是否具有permission
     */
    public boolean hasPermission(String perimission){
        //超级管理员直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        List<String> permsList = SecurityUtils.getLoginUser().getPermsList();
        return permsList != null && permsList.contains(perimission);
    }
}
