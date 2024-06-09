package com.jpozarycki.ragtest.chat.service.impl;

import com.jpozarycki.ragtest.chat.service.MessageHelper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageHelperImpl implements MessageHelper {

    @Override
    public Message getSystemMessage(Resource systemPromptTemplateResource) {
        PromptTemplate promptTemplate = new SystemPromptTemplate(systemPromptTemplateResource);
        return promptTemplate.createMessage();
    }

    @Override
    public Message getMessage(Resource promptTemplateResource, Map<String, Object> propertiesMap) {
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource, propertiesMap);
        return promptTemplate.createMessage();
    }
}
