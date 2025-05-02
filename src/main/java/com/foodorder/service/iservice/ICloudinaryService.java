package com.foodorder.service.iservice;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICloudinaryService {
    public String uploadImage(MultipartFile file) throws IOException;
}
