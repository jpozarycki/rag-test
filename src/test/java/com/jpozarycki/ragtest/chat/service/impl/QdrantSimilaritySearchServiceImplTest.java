package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.service.SimilaritySearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QdrantSimilaritySearchServiceImplTest {
    private static final String QUERY = "What's in the box?";
    private static final List<Document> SIMILAR_DOCUMENTS = List.of(
            new Document("hello"),
            new Document("world"),
            new Document("foo"),
            new Document("bar")
    );
    private static final List<String> SIMILAR_DOCUMENTS_CONTENT = SIMILAR_DOCUMENTS.stream().map(Document::getContent).toList();

    private SimilaritySearchService similaritySearchService;
    @Mock
    private QdrantVectorStore vectorStore;

    @BeforeEach
    void setUp() {
        vectorStore = mock(QdrantVectorStore.class);
        similaritySearchService = new QdrantSimilaritySearchServiceImpl(vectorStore);
    }

    @Test
    void getSimilarDocuments() {
        when(vectorStore.similaritySearch(argThat((ArgumentMatcher<SearchRequest>) sr -> QUERY.equals(sr.getQuery())))).thenReturn(SIMILAR_DOCUMENTS);

        List<String> similarDocuments = similaritySearchService.getSimilarDocuments(QUERY);
        assertThat(similarDocuments).containsExactlyElementsOf(SIMILAR_DOCUMENTS_CONTENT);
    }
}