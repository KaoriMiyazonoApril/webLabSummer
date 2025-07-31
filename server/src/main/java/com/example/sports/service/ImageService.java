package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    OssUtil ossUtil;

    public String upload(MultipartFile file) {
        try {
            return ossUtil.upload(file.getOriginalFilename(), file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw SportsException.fileUploadFail();
        }
    }
}