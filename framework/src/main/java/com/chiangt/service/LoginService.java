package com.chiangt.service;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
