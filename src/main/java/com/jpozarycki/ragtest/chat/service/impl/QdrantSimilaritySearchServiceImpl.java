package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.service.SimilaritySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QdrantSimilaritySearchServiceImpl implements SimilaritySearchService {
    private final QdrantVectorStore vectorStore;
    @Override
    public List<String> getSimilarDocuments(String query) {
        log.info("Searching for similar documents for query: {}", query);
        SearchRequest searchRequest = SearchRequest.query(query)
                .withTopK(15);
        List<String> similarDocs = vectorStore.similaritySearch(searchRequest)
                .stream().map(Document::getContent)
                .toList();
        log.info("Found similar documents: {} for query: {}", similarDocs, query);
        return similarDocs;
    }
}
