package com.codestates.seb006main.util;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileHandler {
    private String fileDir;
    private List<String> extWhiteList = List.of("png","jpg","gif","jpeg","bmp");
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originName = multipartFile.getOriginalFilename();
        String storedName = createStoredName(originName);
//        multipartFile.transferTo(new File(getFullPath(storedName)));

        return storedName;
    }

    public String createStoredName(String originName) {
        String ext = extractedExt(originName);
        if (!extWhiteList.contains(ext.toLowerCase())) {
            throw new BusinessLogicException(ExceptionCode.NOT_ALLOWED_FILENAME_EXTENSION);
        }
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    public String extractedExt(String originName) {
        int pos = originName.lastIndexOf(".");
        return originName.substring(pos + 1);
    }
}
