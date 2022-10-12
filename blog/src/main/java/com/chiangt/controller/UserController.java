package com.chiangt.controller;

import com.chiangt.annotation.SystemLog;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.User;
import com.chiangt.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @SystemLog(businessName = "新用户注册")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

}
