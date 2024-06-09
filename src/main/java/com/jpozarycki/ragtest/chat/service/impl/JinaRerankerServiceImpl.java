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

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JinaRerankerServiceImpl implements RerankerService {
    private final int TOP_N = 5;
    private final double MINIMAL_RELEVANCE = 0.5;
    private final JinaRerankerConfiguration jinaRerankerConfiguration;

    @Override
    public List<String> rerankDocuments(String query, Collection<String> documents) {
        HttpEntity<PostJinaRerankerRequestDTO> request = buildRequest(query, documents);
        RestTemplate restTemplate = new RestTemplate();
        try {
            PostJinaRerankerResponseDTO response = restTemplate.postForObject(jinaRerankerConfiguration.getUrl(), request, PostJinaRerankerResponseDTO.class);

            if (response != null) {
                return response.results().stream()
//                        .filter(result -> result.relevanceScore() >= MINIMAL_RELEVANCE)
                        .map(PostJinaRerankerResponseDTO.Result::document)
                        .map(PostJinaRerankerResponseDTO.Result.Document::text)
                        .toList();
            }
            return List.of();
        } catch (RestClientException e) {
            log.error("Error while calling Jina Reranker");
            throw new RuntimeException(e);
        }
    }

    private HttpEntity<PostJinaRerankerRequestDTO> buildRequest(String query, Collection<String> documents) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(jinaRerankerConfiguration.getApiKey());
        log.info("API KEY: {}", jinaRerankerConfiguration.getApiKey());
        headers.set("Authorization", "Bearer " + jinaRerankerConfiguration.getApiKey());
        PostJinaRerankerRequestDTO request = new PostJinaRerankerRequestDTO(jinaRerankerConfiguration.getModel(), query, List.copyOf(documents), TOP_N);
        return new HttpEntity<>(request, headers);
    }
}
