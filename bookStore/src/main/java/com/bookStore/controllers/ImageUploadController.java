package com.bookStore.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.bookStore.services.PhotoUploadService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class ImageUploadController {

    private final PhotoUploadService photoUploadService = new PhotoUploadService();
    @PostMapping("/v1/upload")
    public void Upload(@RequestParam("file") MultipartFile file){

        var fileName = file.getOriginalFilename();
        var fileType = file.getContentType();
        try {
            photoUploadService.uploadPhoto(file.getInputStream(), fileName, fileType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/v1/download")
    public void Download(){

    }
}
