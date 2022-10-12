package com.chiangt.service;


import com.chiangt.domain.ResponseResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
