package com.jpozarycki.ragtest.chat.service;

import java.util.List;

public interface RerankerService {
    List<String> rerankDocuments(String query, List<String> documents);
}
