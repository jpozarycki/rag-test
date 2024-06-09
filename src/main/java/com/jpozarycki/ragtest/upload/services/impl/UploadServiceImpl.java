package com.jpozarycki.ragtest.upload.services.impl;

import com.jpozarycki.ragtest.documentModel.model.DocumentModel;
import com.jpozarycki.ragtest.upload.enums.UploadStatus;
import com.jpozarycki.ragtest.upload.model.PostUploadRequestDTO;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;
import com.jpozarycki.ragtest.upload.services.DocumentSaveService;
import com.jpozarycki.ragtest.upload.services.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    private final QdrantVectorStore vectorStore;
    private final DocumentSaveService documentSaveService;

    @Override
    public PostUploadResponseDTO uploadToVectorStore(PostUploadRequestDTO postUploadData) {
        try {
            TikaDocumentReader documentReader = new TikaDocumentReader(postUploadData.document().getResource());
            List<Document> documents = documentReader.get();

            TextSplitter textSplitter = new TokenTextSplitter();

            List<Document> splitDocuments = textSplitter.split(documents);
            vectorStore.add(splitDocuments);
            DocumentModel documentModel = documentSaveService.saveDocument(postUploadData.document().getName());
            return new PostUploadResponseDTO(UploadStatus.SUCCESS, documentModel.id());
        } catch (RuntimeException ex) {
            log.error("Error uploading for document", ex);
            return new PostUploadResponseDTO(UploadStatus.ERROR, null);
        }
    }
}
