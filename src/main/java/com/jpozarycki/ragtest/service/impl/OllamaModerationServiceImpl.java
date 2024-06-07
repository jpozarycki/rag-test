package com.jpozarycki.ragtest.service.impl;

import com.jpozarycki.ragtest.enums.ModerationAdequate;
import com.jpozarycki.ragtest.model.GetModerationAdequateResponseDTO;
import com.jpozarycki.ragtest.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OllamaModerationServiceImpl implements ModerationService {
    private final OllamaChatModel chatModel;

    @Value("classpath:templates/moderation-system-prompt.st")
    private Resource moderationSystemPrompt;
    @Value("classpath:templates/moderation-user-prompt.st")
    private Resource moderationUserPrompt;

    @Override
    public boolean isModerationAdequate(String question) {
        BeanOutputConverter<GetModerationAdequateResponseDTO> chatResponseParser = new BeanOutputConverter<>(GetModerationAdequateResponseDTO.class);

        Message systemMessage = getSystemMessage();
        Message userMessage = getUserMessage(question, chatResponseParser);

        ChatResponse chatResponse = chatModel.call(new Prompt(List.of(systemMessage, userMessage)));
        log.info("Moderation response: {}", chatResponse.getResult().getOutput().getContent());
        GetModerationAdequateResponseDTO responseDTO = chatResponseParser.convert(chatResponse.getResult().getOutput().getContent());
        return ModerationAdequate.ACCEPT.equals((responseDTO != null ? responseDTO.adequate() : null) != null ?
                responseDTO.adequate() : ModerationAdequate.REJECT);
    }

    private Message getSystemMessage() {
        PromptTemplate systemPromptTemplate = new PromptTemplate(moderationSystemPrompt);
        return systemPromptTemplate.createMessage();
    }

    private Message getUserMessage(String question, BeanOutputConverter<?> chatResponseParser) {
        PromptTemplate userPromptTemplate = new PromptTemplate(moderationUserPrompt,
                Map.of("prompt", question, "format", chatResponseParser.getFormat()));
        return userPromptTemplate.createMessage();
    }
}
