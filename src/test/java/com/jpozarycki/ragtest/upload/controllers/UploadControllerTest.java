package com.jpozarycki.ragtest.upload.controllers;

import com.jpozarycki.ragtest.upload.enums.UploadStatus;
import com.jpozarycki.ragtest.upload.model.PostUploadRequestDTO;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;
import com.jpozarycki.ragtest.upload.services.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UploadControllerTest {
    private static final String FILE_NAME = "documentName";
    private static final String DOCUMENT_ID = "documentId";
    private static final String FILE_CONTENT = "test content";
    private static final MultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(FILE_NAME, FILE_CONTENT.getBytes(StandardCharsets.UTF_8));

    private UploadController uploadController;

    @Mock
    private UploadService uploadService;

    @BeforeEach
    void setUp() {
        uploadService = mock(UploadService.class);
        uploadController = new UploadController(uploadService);
    }

    @Test
    void upload_returnsOkOnSuccessStatus() {
        PostUploadRequestDTO uploadRequestDTO = new PostUploadRequestDTO(MOCK_MULTIPART_FILE);
        PostUploadResponseDTO uploadResponseDTO = new PostUploadResponseDTO(UploadStatus.SUCCESS, DOCUMENT_ID);

        when(uploadService.uploadToVectorStore(eq(uploadRequestDTO))).thenReturn(uploadResponseDTO);

        ResponseEntity<PostUploadResponseDTO> responseDTO = uploadController.upload(uploadRequestDTO);

        assertNotNull(responseDTO.getBody());
        assertEquals(responseDTO.getBody().status(), UploadStatus.SUCCESS);
        assertEquals(responseDTO.getBody().documentId(), DOCUMENT_ID);
    }

    @Test
    void upload_returnsBadRequestOnErrorStatus() {
        PostUploadRequestDTO uploadRequestDTO = new PostUploadRequestDTO(MOCK_MULTIPART_FILE);
        PostUploadResponseDTO uploadResponseDTO = new PostUploadResponseDTO(UploadStatus.ERROR, null);

        when(uploadService.uploadToVectorStore(eq(uploadRequestDTO))).thenReturn(uploadResponseDTO);

        ResponseEntity<PostUploadResponseDTO> responseDTO = uploadController.upload(uploadRequestDTO);

        assertNotNull(responseDTO.getBody());
        assertEquals(responseDTO.getBody().status(), UploadStatus.ERROR);
        assertNull(responseDTO.getBody().documentId());
    }
}