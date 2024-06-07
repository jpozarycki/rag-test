package com.jpozarycki.ragtest.service;

import java.util.Collection;

public interface PromptService {
    String promptWithDocuments(String query, Collection<String> documents);
}
