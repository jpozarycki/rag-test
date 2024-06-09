package com.jpozarycki.ragtest.upload.services;

import com.jpozarycki.ragtest.documentModel.model.DocumentModel;

public interface DocumentSaveService {
    DocumentModel saveDocument(String fileName);
}
