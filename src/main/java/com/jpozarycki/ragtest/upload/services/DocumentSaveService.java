package com.jpozarycki.ragtest.upload.services;

import com.jpozarycki.ragtest.common.model.DocumentModel;

public interface DocumentSaveService {
    DocumentModel saveDocument(String documentName);
}
