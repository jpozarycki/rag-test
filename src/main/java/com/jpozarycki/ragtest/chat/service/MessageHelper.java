package com.jpozarycki.ragtest.chat.service;

import org.springframework.ai.chat.messages.Message;
import org.springframework.core.io.Resource;

import java.util.Map;

public interface MessageHelper {
    Message getSystemMessage(Resource systemPromptTemplateResource);
    Message getMessage(Resource promptTemplateResource);
    Message getMessage(Resource promptTemplateResource, Map<String, Object> propertiesMap);
}
