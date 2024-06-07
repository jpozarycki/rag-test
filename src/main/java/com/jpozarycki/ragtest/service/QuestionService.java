package com.jpozarycki.ragtest.service;

import com.jpozarycki.ragtest.model.AnswerDTO;

public interface QuestionService {
    AnswerDTO getAnswer(String question);
}
