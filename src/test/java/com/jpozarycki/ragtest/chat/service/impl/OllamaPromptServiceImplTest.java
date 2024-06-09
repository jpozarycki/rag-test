package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.service.PromptService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OllamaPromptServiceImplTest {
    private final String QUESTION = "What's in the box?";
    private final List<String> KNOWLEDGE_DOCS = List.of("Somebody", "once", "told", "me");
    private final String ANSWER = "The world is gonna roll me";
    private PromptService promptService;

    @Mock
    private OllamaChatModel chatModel;

    @BeforeEach
    void setUp() {
        chatModel = mock(OllamaChatModel.class);
        promptService = new OllamaPromptServiceImpl(chatModel);
    }

    @Test
    void promptWithDocuments_callsChatModel() {
        when(chatModel.call(any(Prompt.class))).thenReturn(createResponse(ANSWER));

        String response = promptService.promptWithDocuments(QUESTION, KNOWLEDGE_DOCS);

        assertEquals(ANSWER, response);
        verify(chatModel).call(any(Prompt.class));
    }

    private ChatResponse createResponse(String responseContent) {
        return new ChatResponse(List.of(new Generation(responseContent)));
    }
}