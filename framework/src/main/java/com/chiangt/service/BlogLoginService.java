package com.chiangt.service;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
