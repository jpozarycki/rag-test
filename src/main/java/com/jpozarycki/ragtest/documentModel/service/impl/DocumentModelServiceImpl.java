package com.jpozarycki.ragtest.documentModel.service.impl;

import com.jpozarycki.ragtest.documentModel.repositories.DocumentModelRepository;
import com.jpozarycki.ragtest.documentModel.service.DocumentModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentModelServiceImpl implements DocumentModelService {
    private final DocumentModelRepository documentModelRepository;

    @Override
    public boolean isDocumentModelExist(String documentId) {
        return documentModelRepository.existsById(documentId);
    }
}
