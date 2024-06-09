package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.service.MessageHelper;
import com.jpozarycki.ragtest.chat.service.PromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OllamaPromptServiceImpl implements PromptService {
    private static final String KNOWLEDGE_SOURCE_PLACEHOLDER = "{KNOWLEDGE_SOURCE}";
    private static final String SYSTEM_PROMPT_TEMPLATE = """
            You are an AI assistant whose purpose is to answer questions using only the knowledge provided to you. \
            You cannot make any assumptions or use any outside information. \
            \
            Carefully review the provided knowledge and determine if the question can be answered using only the facts and information given. \
            Do not make any assumptions or use any outside knowledge. \
            Remember, you must use only the provided knowledge and cannot make any assumptions or use outside information. \
            If the knowledge is not sufficient to answer the question, say so. \
            Here is the knowledge you have been given: \
            ``` \
            """
            + KNOWLEDGE_SOURCE_PLACEHOLDER
            + """
            ``` \
            """;
    private final OllamaChatModel chatModel;

    @Override
    public String promptWithDocuments(String query, Collection<String> documents) {
        String knowledgeSource = String.join(StringUtils.SPACE, documents);
        log.info("Prompting with documents: {} for query: {}", documents, query);

        String systemPrompt = SYSTEM_PROMPT_TEMPLATE.replace(KNOWLEDGE_SOURCE_PLACEHOLDER, knowledgeSource);
        Message systemPromptTemplate = new SystemPromptTemplate(systemPrompt).createMessage();
        Message userPrompt = new PromptTemplate(query).createMessage();

        ChatResponse chatResponse = chatModel.call(new Prompt(List.of(systemPromptTemplate, userPrompt)));
        String responseContent = chatResponse.getResult().getOutput().getContent();
        log.info("Prompt response: {} for query: {}", responseContent, query);
        return responseContent;
    }
}
