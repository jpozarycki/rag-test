package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.helpers.Answers;
import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {
    private static final String DOCUMENT_ID = "documentId";
    private static final String QUESTION = "What's in the box?";
    private static final String ANSWER = "Gummy bears!";
    private static final List<String> SIMILAR_DOCUMENTS = List.of("hello", "world", "foo", "bar");
    private static final List<String> RERANKED_DOCUMENTS = List.of("foo", "hello", "world", "bar");

    private ChatService chatService;
    @Mock
    private ModerationService moderationService;
    @Mock
    private SimilaritySearchService similaritySearchService;
    @Mock
    private RerankerService rerankerService;
    @Mock
    private PromptService promptService;

    @BeforeEach
    void setUp() {
        moderationService = mock(ModerationService.class);
        similaritySearchService = mock(SimilaritySearchService.class);
        rerankerService = mock(RerankerService.class);
        promptService = mock(PromptService.class);
        chatService = new ChatServiceImpl(moderationService, similaritySearchService, rerankerService, promptService);
    }

    @Test
    void getAnswer_returnsAnswer() {
        when(moderationService.isModerationAdequate(eq(QUESTION))).thenReturn(true);
        when(similaritySearchService.getSimilarDocuments(eq(QUESTION))).thenReturn(SIMILAR_DOCUMENTS);
        when(rerankerService.rerankDocuments(eq(QUESTION), eq(SIMILAR_DOCUMENTS))).thenReturn(RERANKED_DOCUMENTS);
        when(promptService.promptWithDocuments(eq(QUESTION), eq(RERANKED_DOCUMENTS))).thenReturn(ANSWER);

        AnswerDTO answerDTO = chatService.getAnswer(QUESTION);

        assertEquals(ANSWER, answerDTO.answer());
    }

    @Test
    void getAnswer_returnsDenial() {
        when(moderationService.isModerationAdequate(eq(QUESTION))).thenReturn(false);

        AnswerDTO answerDTO = chatService.getAnswer(QUESTION);

        assertEquals(Answers.DENIAL, answerDTO.answer());
        verify(similaritySearchService, never()).getSimilarDocuments(any());
        verify(rerankerService, never()).rerankDocuments(any(), any());
        verify(promptService, never()).promptWithDocuments(any(), any());
    }

    @Test
    void getAnswer_returnsDenialWhenSimilarDocumentsIsEmpty() {
        when(moderationService.isModerationAdequate(eq(QUESTION))).thenReturn(true);
        when(similaritySearchService.getSimilarDocuments(eq(QUESTION))).thenReturn(List.of());

        AnswerDTO answerDTO = chatService.getAnswer(QUESTION);

        assertEquals(Answers.DENIAL, answerDTO.answer());
        verify(rerankerService, never()).rerankDocuments(any(), any());
        verify(promptService, never()).promptWithDocuments(any(), any());
    }

    @Test
    void getAnswer_returnsDenialWhenRerankedDocumentsIsEmpty() {
        when(moderationService.isModerationAdequate(eq(QUESTION))).thenReturn(true);
        when(similaritySearchService.getSimilarDocuments(eq(QUESTION))).thenReturn(SIMILAR_DOCUMENTS);
        when(rerankerService.rerankDocuments(eq(QUESTION), eq(SIMILAR_DOCUMENTS))).thenReturn(List.of());

        AnswerDTO answerDTO = chatService.getAnswer(QUESTION);

        assertEquals(Answers.DENIAL, answerDTO.answer());
        verify(promptService, never()).promptWithDocuments(any(), any());
    }
}