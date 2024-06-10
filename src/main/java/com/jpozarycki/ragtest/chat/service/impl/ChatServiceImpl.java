package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jpozarycki.ragtest.helpers.Answers.DENIAL;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final ModerationService moderationService;
    private final SimilaritySearchService similaritySearchService;
    private final RerankerService rerankerService;
    private final PromptService promptService;

    @Override
    public AnswerDTO getAnswer(String question) {
        if (!moderationService.isModerationAdequate(question)) {
            return new AnswerDTO(DENIAL);
        }

        List<String> similarDocuments = similaritySearchService.getSimilarDocuments(question);

        if (similarDocuments.isEmpty()) {
            return new AnswerDTO(DENIAL);
        }

        List<String> rerankedDocuments = rerankerService.rerankDocuments(question, similarDocuments);

        if (rerankedDocuments.isEmpty()) {
            return new AnswerDTO(DENIAL);
        }

        String answer = promptService.promptWithDocuments(question, rerankedDocuments);

        return new AnswerDTO(answer);
    }
}
