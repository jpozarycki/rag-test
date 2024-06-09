package com.jpozarycki.ragtest.chat.service;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.model.QuestionDTO;

public interface ChatService {
    AnswerDTO getAnswer(QuestionDTO question);
}
