package com.jpozarycki.ragtest.chat.service;

import java.util.List;

public interface SimilaritySearchService {
    List<String> getSimilarDocuments(String query);
}
