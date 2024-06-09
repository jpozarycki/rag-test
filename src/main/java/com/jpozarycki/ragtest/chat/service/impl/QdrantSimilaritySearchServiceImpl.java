package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.service.SimilaritySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QdrantSimilaritySearchServiceImpl implements SimilaritySearchService {
    private final QdrantVectorStore vectorStore;
    @Override
    public List<String> getSimilarDocuments(String query) {
        SearchRequest searchRequest = SearchRequest.query(query)
                .withTopK(15);
        return vectorStore.similaritySearch(searchRequest)
                .stream().map(Document::getContent)
                .toList();
    }
}
