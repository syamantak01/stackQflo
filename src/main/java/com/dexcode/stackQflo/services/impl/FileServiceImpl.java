package com.dexcode.stackQflo.services.impl;

import com.dexcode.stackQflo.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // File name
        String name = file.getOriginalFilename();

        // random file name generated - Done to prevent SQL injection attack
        String randomID = UUID.randomUUID().toString();

        String fileName = randomID.concat(name.substring(name.lastIndexOf(".")));

        // full path
        String filePath = path + File.separator + fileName;

        // Create folder if not created
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        // Copy files
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;

    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);

        //db logic to return inputstream

        return is;
    }
}
