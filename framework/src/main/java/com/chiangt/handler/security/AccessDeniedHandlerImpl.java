package com.chiangt.handler.security;

import com.alibaba.fastjson.JSON;
import com.chiangt.domain.ResponseResult;
import com.chiangt.enums.AppHttpCodeEnum;
import com.chiangt.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATION_AUTH);
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
    }
}
