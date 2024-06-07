package com.jpozarycki.ragtest.service.impl;

import com.jpozarycki.ragtest.model.AnswerDTO;
import com.jpozarycki.ragtest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {
    private static final String MODERATION_INADEQUATE_MESSAGE = "I'm sorry, I can't answer that question.";
    private final ModerationService moderationService;
    private final StatementProviderService statementProviderService;
    private final SimilaritySearchService similaritySearchService;
    private final RerankerService rerankerService;
    private final PromptService promptService;
    @Override
    public AnswerDTO getAnswer(String question) {
        if (!moderationService.isModerationAdequate(question)) {
            return new AnswerDTO(MODERATION_INADEQUATE_MESSAGE);
        }

        String statement = statementProviderService.getStatement(question);
        List<String> documents = similaritySearchService.getSimilarDocuments(statement);
        List<String> rerankedDocuments = rerankerService.rerankDocuments(statement, documents);
        String answer = promptService.promptWithDocuments(question, rerankedDocuments);

        return new AnswerDTO(answer);
    }
}
