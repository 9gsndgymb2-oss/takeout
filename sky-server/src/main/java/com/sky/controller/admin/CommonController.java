package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result upload(MultipartFile file)
    {
        String Name = file.getOriginalFilename();
        String last = Name.substring(Name.lastIndexOf( "."));
        String objectName = UUID.randomUUID().toString() + last;

        try {
            String url = aliOssUtil.upload(file.getBytes(),objectName);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        log.info("文件上传：{}", file);

        return Result.error("上传失败");
    }
}
