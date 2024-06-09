package com.jpozarycki.ragtest.upload.model;

import org.springframework.web.multipart.MultipartFile;

public record PostUploadRequestDTO(String fileName, MultipartFile document) {
}
