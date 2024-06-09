package com.jpozarycki.ragtest.chat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpozarycki.ragtest.chat.enums.ModerationAdequate;
import com.jpozarycki.ragtest.chat.model.GetModerationAdequateResponseDTO;
import com.jpozarycki.ragtest.chat.service.MessageHelper;
import com.jpozarycki.ragtest.chat.service.ModerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OllamaModerationServiceImplTest {
    private static final String SYSTEM_PROMPT = "I'm moderating this chat. Please be nice.";
    private static final String QUESTION = "What's in the box?";
    private static final Message SYSTEM_MESSAGE = new SystemMessage(SYSTEM_PROMPT);
    private static final Message USER_MESSAGE = new SystemMessage(QUESTION);

    private ModerationService moderationService;
    @Mock
    private OllamaChatModel chatModel;
    @Mock
    private MessageHelper messageHelper;

    @BeforeEach
    void setUp() {
        chatModel = mock(OllamaChatModel.class);
        messageHelper = mock(MessageHelper.class);
        moderationService = new OllamaModerationServiceImpl(chatModel, messageHelper);

        when(messageHelper.getSystemMessage(any())).thenReturn(SYSTEM_MESSAGE);
        when(messageHelper.getMessage(any(), anyMap())).thenReturn(USER_MESSAGE);
    }

    @Test
    void isModerationAdequate_returnsTrueIfMessageIsAccepted() throws IOException {
        when(chatModel.call(any(Prompt.class))).thenReturn(createResponse(ModerationAdequate.ACCEPT));

        boolean isModerationAdequate = moderationService.isModerationAdequate(QUESTION);

        assertTrue(isModerationAdequate);
    }

    @Test
    void isModerationAdequate_returnsFalseIfMessageIsRejected() throws IOException {
        when(chatModel.call(any(Prompt.class))).thenReturn(createResponse(ModerationAdequate.REJECT));

        boolean isModerationAdequate = moderationService.isModerationAdequate(QUESTION);

        assertFalse(isModerationAdequate);
    }

    private ChatResponse createResponse(ModerationAdequate adequate) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(new GetModerationAdequateResponseDTO(adequate));
        return new ChatResponse(List.of(new Generation(response)));
    }

}