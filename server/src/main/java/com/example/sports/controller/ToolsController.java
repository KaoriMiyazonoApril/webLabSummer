package com.example.sports.controller;

import com.example.sports.service.ImageService;
import com.example.sports.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ToolsController {
    @Autowired
    ImageService imageService;

    @PostMapping("")
    public Response<String> upload(@RequestParam MultipartFile file){
        return Response.buildSuccess(imageService.upload(file));
    }

}