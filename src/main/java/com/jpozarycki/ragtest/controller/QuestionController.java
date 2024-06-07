package com.jpozarycki.ragtest.controller;

import com.jpozarycki.ragtest.model.AnswerDTO;
import com.jpozarycki.ragtest.model.QuestionDTO;
import com.jpozarycki.ragtest.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/question")
    public AnswerDTO getAnswer(@RequestBody QuestionDTO question) {
        return questionService.getAnswer(question.question());
    }
}
