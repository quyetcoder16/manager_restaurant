package com.promise.manager_restaurant.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FilesStorageService {
    public String saveFile(MultipartFile file);
    Resource loadFile(String fileName);
}
