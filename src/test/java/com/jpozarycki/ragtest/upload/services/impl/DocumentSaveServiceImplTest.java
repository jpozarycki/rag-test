package com.jpozarycki.ragtest.upload.services.impl;

import com.jpozarycki.ragtest.documentModel.model.DocumentModel;
import com.jpozarycki.ragtest.documentModel.repositories.DocumentModelRepository;
import com.jpozarycki.ragtest.upload.services.DocumentSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentSaveServiceImplTest {
    private static final String FILE_NAME = "documentName";
    private static final String DOCUMENT_ID = "documentId";

    private DocumentSaveService documentSaveService;

    @Mock
    private DocumentModelRepository documentModelRepository;

    @BeforeEach
    void setUp() {
        documentModelRepository = mock(DocumentModelRepository.class);
        documentSaveService = new DocumentSaveServiceImpl(documentModelRepository);
    }

    @Test
    void saveDocument_returnsDocumentWithSameFileName() {
        DocumentModel testDocumentModel = new DocumentModel(DOCUMENT_ID, FILE_NAME);
        when(documentModelRepository.save(any(DocumentModel.class))).thenReturn(testDocumentModel);

        DocumentModel savedDocumentModel = documentSaveService.saveDocument(FILE_NAME);

        assertEquals(savedDocumentModel.fileName(), FILE_NAME);
    }
}