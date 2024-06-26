package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.configuration.JinaRerankerConfiguration;
import com.jpozarycki.ragtest.chat.model.PostJinaRerankerRequestDTO;
import com.jpozarycki.ragtest.chat.model.PostJinaRerankerResponseDTO;
import com.jpozarycki.ragtest.chat.service.RerankerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JinaRerankerServiceImpl implements RerankerService {
    private final int TOP_N = 5;
    private final JinaRerankerConfiguration jinaRerankerConfiguration;
    private final RestTemplate restTemplate;

    @Override
    public List<String> rerankDocuments(String query, List<String> documents) {
        log.info("Reranking documents: {} for query: {}", documents, query);
        HttpEntity<PostJinaRerankerRequestDTO> request = buildRequest(query, documents);
        try {
            PostJinaRerankerResponseDTO response = restTemplate.postForObject(jinaRerankerConfiguration.getUrl(), request, PostJinaRerankerResponseDTO.class);

            if (response != null) {
                List<String> rerankedDocs = response.results().stream()
                        .map(PostJinaRerankerResponseDTO.Result::document)
                        .map(PostJinaRerankerResponseDTO.Result.Document::text)
                        .toList();
                log.info("Reranked documents: {} for query: {}", rerankedDocs, query);
                return rerankedDocs;
            }
            return List.of();
        } catch (RestClientException e) {
            log.error("Error while calling Jina Reranker", e);
            return List.of();
        }
    }

    private HttpEntity<PostJinaRerankerRequestDTO> buildRequest(String query, List<String> documents) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jinaRerankerConfiguration.getApiKey());
        PostJinaRerankerRequestDTO request = new PostJinaRerankerRequestDTO(jinaRerankerConfiguration.getModel(), query, documents, TOP_N);
        return new HttpEntity<>(request, headers);
    }
}
