package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.model.QuestionDTO;
import com.jpozarycki.ragtest.chat.service.*;
import com.jpozarycki.ragtest.common.repositories.DocumentModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private static final String DENIAL_ANSWER = "I'm sorry, I can't answer that question.";
    private final ModerationService moderationService;
    private final SimilaritySearchService similaritySearchService;
    private final RerankerService rerankerService;
    private final PromptService promptService;
    private final DocumentModelRepository documentModelRepository;

    @Override
    public AnswerDTO getAnswer(QuestionDTO question) {
        String questionText = question.question();
        if (!documentExists(question.documentId()) || !moderationService.isModerationAdequate(questionText)) {
            return new AnswerDTO(DENIAL_ANSWER);
        }

        List<String> documents = similaritySearchService.getSimilarDocuments(questionText);
        List<String> rerankedDocuments = rerankerService.rerankDocuments(questionText, documents);

        if (rerankedDocuments.isEmpty()) {
            return new AnswerDTO(DENIAL_ANSWER);
        }

        String answer = promptService.promptWithDocuments(questionText, rerankedDocuments);

        return new AnswerDTO(answer);
    }

    private boolean documentExists(String documentId) {
        return documentModelRepository.existsById(documentId);
    }
}
