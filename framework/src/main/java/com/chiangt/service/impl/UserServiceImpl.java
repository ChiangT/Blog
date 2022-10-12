package com.chiangt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.User;
import com.chiangt.domain.entity.UserRole;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.domain.vo.UserInfoVo;
import com.chiangt.domain.vo.UserVo;
import com.chiangt.enums.AppHttpCodeEnum;
import com.chiangt.exception.SystemException;
import com.chiangt.mapper.UserMapper;
import com.chiangt.service.UserRoleService;
import com.chiangt.utils.BeanCopyUtils;
import com.chiangt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chiangt.service.UserService;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-10-05 15:08:18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult register(User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(fieldExist(user.getUserName(), "username")){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(fieldExist(user.getNickName(), "nickname")) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        String encodePasswd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePasswd);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUserPage(Integer pageNum, Integer pageSize, User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        queryWrapper.like(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());
        queryWrapper.like(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<User> userList = page.getRecords();
        List<UserVo> userVoList = BeanCopyUtils.copyBeanList(userList, UserVo.class);
        PageVo pageVo = new PageVo();
        pageVo.setRows(userVoList);
        pageVo.setTotal(page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if(!checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(!checkPhoneUnique(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if(!checkEmailUnique(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        if(user.getRoleIds() != null && user.getRoleIds().length > 0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    public void updateUser(User user) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(queryWrapper);
        insertUserRole(user);
        updateById(user);
    }

    private void insertUserRole(User user) {
        List<UserRole> userRoleList = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    private boolean checkEmailUnique(String email) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail, email)) == 0;
    }

    private boolean checkPhoneUnique(String phonenumber) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber, phonenumber)) == 0;
    }

    private boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName)) == 0;
    }

    private boolean fieldExist(String value, String field){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if("username".equals(field)){
            queryWrapper.eq(User::getUserName, value);
        }
        if("nickname".equals(field)){
            queryWrapper.eq(User::getNickName, value);
        }
        int count = count(queryWrapper);
        return count > 0;
    }
}

