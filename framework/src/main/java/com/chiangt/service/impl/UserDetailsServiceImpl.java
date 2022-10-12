package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chiangt.constants.SystemConstants;
import com.chiangt.domain.entity.LoginUser;
import com.chiangt.domain.entity.User;
import com.chiangt.mapper.LinkMapper;
import com.chiangt.mapper.MenuMapper;
import com.chiangt.mapper.UserMapper;
import com.chiangt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> permList = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, permList);
        }
        return new LoginUser(user, null);
    }
}
