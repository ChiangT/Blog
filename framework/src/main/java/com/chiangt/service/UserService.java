package com.chiangt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-10-05 15:08:17
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult register(User user);

    ResponseResult selectUserPage(Integer pageNum, Integer pageSize, User user);

    ResponseResult addUser(User user);

    void updateUser(User user);
}

