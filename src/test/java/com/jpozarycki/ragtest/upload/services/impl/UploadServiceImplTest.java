package com.jpozarycki.ragtest.upload.services.impl;

import com.jpozarycki.ragtest.documentModel.model.DocumentModel;
import com.jpozarycki.ragtest.upload.enums.UploadStatus;
import com.jpozarycki.ragtest.upload.model.PostUploadRequestDTO;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;
import com.jpozarycki.ragtest.upload.services.DocumentSaveService;
import com.jpozarycki.ragtest.upload.services.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UploadServiceImplTest {
    private static final String FILE_NAME = "documentName";
    private static final String DOCUMENT_ID = "documentId";
    private static final String FILE_CONTENT = "test content";
    private static final MultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(FILE_NAME, FILE_CONTENT.getBytes(StandardCharsets.UTF_8));

    private UploadService uploadService;
    @Mock
    private DocumentSaveService documentSaveService;
    @Mock
    private QdrantVectorStore qdrantVectorStore;

    @BeforeEach
    void setUp() {
        documentSaveService = mock(DocumentSaveService.class);
        qdrantVectorStore = mock(QdrantVectorStore.class);
        uploadService = new UploadServiceImpl(qdrantVectorStore, documentSaveService);
    }

    @Test
    void uploadToVectorStore_returnsOkOnSuccess() {
        DocumentModel testDocumentModel = new DocumentModel(DOCUMENT_ID, FILE_NAME);
        PostUploadRequestDTO testPostUploadRequestDTO = new PostUploadRequestDTO(MOCK_MULTIPART_FILE);

        when(documentSaveService.saveDocument(eq(FILE_NAME))).thenReturn(testDocumentModel);

        PostUploadResponseDTO responseDTO = uploadService.uploadToVectorStore(testPostUploadRequestDTO);

        assertEquals(responseDTO.status(), UploadStatus.OK);
        assertEquals(responseDTO.documentId(), DOCUMENT_ID);
    }

    @Test
    void uploadToVectorStore_returnsErrorOnException() {
        PostUploadRequestDTO testPostUploadRequestDTO = new PostUploadRequestDTO(MOCK_MULTIPART_FILE);

        when(documentSaveService.saveDocument(eq(FILE_NAME))).thenThrow(new RuntimeException());

        PostUploadResponseDTO responseDTO = uploadService.uploadToVectorStore(testPostUploadRequestDTO);

        assertEquals(responseDTO.status(), UploadStatus.ERROR);
        assertNull(responseDTO.documentId());
    }
}