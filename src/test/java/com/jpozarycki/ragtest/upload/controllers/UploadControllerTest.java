package com.jpozarycki.ragtest.upload.controllers;

import com.jpozarycki.ragtest.upload.enums.UploadStatus;
import com.jpozarycki.ragtest.upload.model.PostUploadRequestDTO;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;
import com.jpozarycki.ragtest.upload.services.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
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
        PostUploadResponseDTO uploadResponseDTO = new PostUploadResponseDTO(UploadStatus.SUCCESS, DOCUMENT_ID, null);

        when(uploadService.uploadToVectorStore(eq(uploadRequestDTO))).thenReturn(uploadResponseDTO);

        ResponseEntity<PostUploadResponseDTO> responseDTO = uploadController.upload(uploadRequestDTO);

        assertNotNull(responseDTO.getBody());
        assertEquals(HttpStatus.OK.value(), responseDTO.getStatusCode().value());
        assertEquals(UploadStatus.SUCCESS, responseDTO.getBody().status());
        assertEquals(DOCUMENT_ID, responseDTO.getBody().documentId());
    }

    @Test
    void upload_returnsBadRequestOnErrorStatus() {
        PostUploadRequestDTO uploadRequestDTO = new PostUploadRequestDTO(MOCK_MULTIPART_FILE);
        PostUploadResponseDTO uploadResponseDTO = new PostUploadResponseDTO(UploadStatus.ERROR, null, null);

        when(uploadService.uploadToVectorStore(eq(uploadRequestDTO))).thenReturn(uploadResponseDTO);

        ResponseEntity<PostUploadResponseDTO> responseDTO = uploadController.upload(uploadRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseDTO.getStatusCode().value());
        assertNotNull(responseDTO.getBody());
        assertEquals(UploadStatus.ERROR, responseDTO.getBody().status());
        assertNull(responseDTO.getBody().documentId());
    }

    @Test
    void upload_returnsBadRequestOnNullDocument() {
        PostUploadRequestDTO uploadRequestDTO = new PostUploadRequestDTO(null);

        ResponseEntity<PostUploadResponseDTO> responseDTO = uploadController.upload(uploadRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseDTO.getStatusCode().value());
        assertNotNull(responseDTO.getBody());
        assertEquals(UploadStatus.ERROR, responseDTO.getBody().status());
        assertNull(responseDTO.getBody().documentId());
    }
}