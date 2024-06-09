package com.jpozarycki.ragtest.chat.service;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;

public interface ChatService {
    AnswerDTO getAnswer(String question);
}
