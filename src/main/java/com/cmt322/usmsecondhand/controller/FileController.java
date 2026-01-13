package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    // 读取配置文件里的上传路径，本地开发和服务器部署可以不一样
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 读取域名，用于拼接返回的 URL
    @Value("${file.domain}")
    private String domain;

    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "File is empty");
        }

        // 1. 生成唯一文件名 (防止文件名冲突)
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;

        // 2. 创建文件对象
        File dest = new File(uploadDir + fileName);
        
        // 3. 检查目录是否存在，不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            // 4. 保存文件到磁盘
            file.transferTo(dest);
            
            // 5. 返回可访问的 URL (例如: http://localhost:8081/api/images/uuid.jpg)
            // 注意：这里的 /images/ 是我们在 WebMvcConfig 里配置的映射路径，或者 Nginx 配置的
            String fileUrl = domain + "/images/" + fileName;
            
            return ResultUtils.success(fileUrl);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Upload failed");
        }
    }
}