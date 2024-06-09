package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.configuration.JinaRerankerConfiguration;
import com.jpozarycki.ragtest.chat.model.PostJinaRerankerRequestDTO;
import com.jpozarycki.ragtest.chat.model.PostJinaRerankerResponseDTO;
import com.jpozarycki.ragtest.chat.service.RerankerService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JinaRerankerServiceImplTest {
    private static final String API_URL = "http://localhost:1234";
    private static final String API_KEY = "1234";
    private static final String MODEL = "model";
    private static final String QUERY = "What's in the box?";
    private static final List<String> SIMILAR_DOCUMENTS = List.of("hello", "world", "foo", "bar");
    private static final List<String> RERANKED_DOCUMENTS = List.of("foo", "hello", "world", "bar");

    private RerankerService jinaRerankerService;
    @Mock
    private JinaRerankerConfiguration jinaRerankerConfiguration;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        jinaRerankerConfiguration = mock(JinaRerankerConfiguration.class);
        restTemplate = mock(RestTemplate.class);
        when(jinaRerankerConfiguration.getUrl()).thenReturn(API_URL);
        when(jinaRerankerConfiguration.getApiKey()).thenReturn(API_KEY);
        when(jinaRerankerConfiguration.getModel()).thenReturn(MODEL);
        jinaRerankerService = new JinaRerankerServiceImpl(jinaRerankerConfiguration, restTemplate);
    }

    @Test
    void rerankDocuments_returnsRerankedDocuments() {
        when(restTemplate.postForObject(eq(API_URL), argThat(rerankerRequestMatcher(QUERY,SIMILAR_DOCUMENTS)), eq(PostJinaRerankerResponseDTO.class))).thenReturn(createResponse(RERANKED_DOCUMENTS));
        List<String> rerankedDocuments = jinaRerankerService.rerankDocuments(QUERY, SIMILAR_DOCUMENTS);

        assertThat(rerankedDocuments).containsExactlyElementsOf(RERANKED_DOCUMENTS);
    }

    @Test
    void rerankDocuments_returnsEmptyListIfResponseEmpty() {
        when(restTemplate.postForObject(eq(API_URL), argThat(rerankerRequestMatcher(QUERY,SIMILAR_DOCUMENTS)), eq(PostJinaRerankerResponseDTO.class))).thenReturn(null);
        List<String> rerankedDocuments = jinaRerankerService.rerankDocuments(QUERY, SIMILAR_DOCUMENTS);

        assertThat(rerankedDocuments).isEmpty();
    }

    @Test
    void rerankDocuments_returnsEmptyListIfRestClientException() {
        when(restTemplate.postForObject(eq(API_URL), argThat(rerankerRequestMatcher(QUERY,SIMILAR_DOCUMENTS)), eq(PostJinaRerankerResponseDTO.class))).thenThrow(new RestClientException(StringUtils.EMPTY));
        List<String> rerankedDocuments = jinaRerankerService.rerankDocuments(QUERY, SIMILAR_DOCUMENTS);

        assertThat(rerankedDocuments).isEmpty();
    }

    private ArgumentMatcher<HttpEntity<PostJinaRerankerRequestDTO>> rerankerRequestMatcher(String query, List<String> documents) {
        return request -> {
            PostJinaRerankerRequestDTO body = request.getBody();
            return body != null && body.query().equals(query) && body.documents().equals(documents);
        };
    }

    private PostJinaRerankerResponseDTO createResponse(List<String> documents) {
        return new PostJinaRerankerResponseDTO(MODEL,
                new PostJinaRerankerResponseDTO.Usage(50, 10),
                documents.stream()
                        .map(d -> new PostJinaRerankerResponseDTO.Result(0, new PostJinaRerankerResponseDTO.Result.Document(d), 0.6d))
                        .toList());
    }
}