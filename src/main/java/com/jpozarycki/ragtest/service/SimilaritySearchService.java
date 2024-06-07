package com.jpozarycki.ragtest.service;

import java.util.List;

public interface SimilaritySearchService {
    List<String> getSimilarDocuments(String query);
}
