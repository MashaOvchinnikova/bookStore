package com.bookStore.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.util.IOUtils;
import com.bookStore.services.PhotoUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping(value = "/v1/download/{fileName}")
    @ResponseBody
    public void GetImage(@PathVariable String fileName,  HttpServletResponse response){
        response.setContentType("image/png");
        try {
            var file = photoUploadService.downloadPhoto(fileName);
            InputStream temp = new ByteArrayInputStream(file);
            IOUtils.copy(temp, response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/v1/get_link/{fileName}")
    public String GetLink(@PathVariable String fileName){
        try {
            var link = photoUploadService.getImageLink(fileName);
            return link;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/admin/image_upload")
    public String imgUploadTest()
    {
        return "imgTest";
    }
}
