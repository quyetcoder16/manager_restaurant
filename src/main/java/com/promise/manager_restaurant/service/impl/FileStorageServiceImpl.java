package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.service.FilesStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class FileStorageServiceImpl implements FilesStorageService {

    @Value("${fileUpload.rootPath}")
    private String rootPath;

    private Path root;

    public void init() {
        try {
            root = Paths.get(rootPath);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (Exception e) {
            log.error("error create folder root" + e.getMessage());
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        try {
            init();
            // Lấy tên file gốc
            String originalFilename = file.getOriginalFilename();

            // Tạo định dạng ngày tháng
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Tách phần tên và phần mở rộng của file
            String fileName = "";
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                int dotIndex = originalFilename.lastIndexOf(".");
                fileName = originalFilename.substring(0, dotIndex);
                extension = originalFilename.substring(dotIndex);
            } else {
                fileName = originalFilename;
            }

            // Tạo tên file mới với timestamp
            String newFileName = fileName + "_" + timestamp + extension;

            // Lưu file với tên mới
            Files.copy(file.getInputStream(),
                    root.resolve(newFileName),
                    StandardCopyOption.REPLACE_EXISTING); // Ghi đè nếu file tồn tại

            return newFileName;
        } catch (Exception e) {
            System.out.println("error save file" + e.getMessage());
            return null;
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            init();
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (Exception e) {
            log.error("error load file" + e.getMessage());
        }
        return null;
    }
}
