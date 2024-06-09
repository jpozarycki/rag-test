package com.jpozarycki.ragtest.upload.services.impl;

import com.jpozarycki.ragtest.common.model.DocumentModel;
import com.jpozarycki.ragtest.common.repositories.DocumentModelRepository;
import com.jpozarycki.ragtest.upload.services.DocumentSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DocumentSaveServiceImpl implements DocumentSaveService {
    private final DocumentModelRepository documentModelRepository;
    @Override
    public DocumentModel saveDocument(String fileName) {
        DocumentModel documentModel = new DocumentModel(UUID.randomUUID().toString(), fileName);
        return documentModelRepository.save(documentModel);
    }
}
