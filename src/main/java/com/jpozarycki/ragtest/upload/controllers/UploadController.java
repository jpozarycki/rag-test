package com.jpozarycki.ragtest.upload.controllers;

import com.jpozarycki.ragtest.upload.enums.UploadStatus;
import com.jpozarycki.ragtest.upload.model.PostUploadRequestDTO;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;
import com.jpozarycki.ragtest.upload.services.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostUploadResponseDTO> upload(@ModelAttribute PostUploadRequestDTO postUploadRequestDTO) {
        if (postUploadRequestDTO.document() == null || postUploadRequestDTO.document().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        PostUploadResponseDTO postUploadResponseDTO = uploadService.uploadToVectorStore(postUploadRequestDTO);
        if (UploadStatus.SUCCESS.equals(postUploadResponseDTO.status())) {
            return ResponseEntity.ok(postUploadResponseDTO);
        }
        return ResponseEntity.badRequest().body(postUploadResponseDTO);
    }
}
