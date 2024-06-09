package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.enums.ModerationAdequate;
import com.jpozarycki.ragtest.chat.model.GetModerationAdequateResponseDTO;
import com.jpozarycki.ragtest.chat.service.MessageHelper;
import com.jpozarycki.ragtest.chat.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
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
    private final MessageHelper messageHelper;

    @Value("classpath:templates/moderation-system-prompt.st")
    private Resource moderationSystemPrompt;
    @Value("classpath:templates/moderation-user-prompt.st")
    private Resource moderationUserPrompt;

    @Override
    public boolean isModerationAdequate(String question) {
        BeanOutputConverter<GetModerationAdequateResponseDTO> chatResponseParser = new BeanOutputConverter<>(GetModerationAdequateResponseDTO.class);

        Message systemMessage = messageHelper.getMessage(moderationSystemPrompt);
        Message userMessage = messageHelper.getMessage(moderationUserPrompt, Map.of("prompt", question, "format", chatResponseParser.getFormat()));

        ChatResponse chatResponse = chatModel.call(new Prompt(List.of(systemMessage, userMessage)));
        String responseContent = chatResponse.getResult().getOutput().getContent();
        log.info("Moderation response: {}", responseContent);
        GetModerationAdequateResponseDTO responseDTO = chatResponseParser.convert(responseContent);
        return ModerationAdequate.ACCEPT.equals((responseDTO != null ? responseDTO.adequate() : null) != null ? responseDTO.adequate() : ModerationAdequate.REJECT);
    }
}
