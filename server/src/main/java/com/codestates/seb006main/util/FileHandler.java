package com.codestates.seb006main.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileHandler {
    private String fileDir;

    // TODO:
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originName = multipartFile.getOriginalFilename();
        String storedName = createStoredName(originName);
        multipartFile.transferTo(new File(getFullPath(storedName)));

        return storedName;
    }

    public String createStoredName(String originName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractedExt(originName);
        return uuid + "." + ext;
    }

    public String extractedExt(String originName) {
        int pos = originName.lastIndexOf(".");
        return originName.substring(pos + 1);
    }
}
