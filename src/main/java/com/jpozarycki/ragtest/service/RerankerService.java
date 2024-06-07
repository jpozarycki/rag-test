package com.jpozarycki.ragtest.service;

import java.util.Collection;
import java.util.List;

public interface RerankerService {
    List<String> rerankDocuments(String query, Collection<String> documents);
}
