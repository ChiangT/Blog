package com.chiangt.service.impl;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.LoginUser;
import com.chiangt.domain.entity.User;
import com.chiangt.domain.vo.BlogUserLoginVo;
import com.chiangt.domain.vo.UserInfoVo;
import com.chiangt.service.BlogLoginService;
import com.chiangt.service.LoginService;
import com.chiangt.utils.BeanCopyUtils;
import com.chiangt.utils.JwtUtil;
import com.chiangt.utils.RedisCache;
import com.chiangt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("adminlogin:"+userId,loginUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("adminlogin:"+userId);
        return ResponseResult.okResult();
    }

}
