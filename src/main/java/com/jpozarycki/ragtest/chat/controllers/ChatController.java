package com.jpozarycki.ragtest.chat.controllers;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.model.QuestionDTO;
import com.jpozarycki.ragtest.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/question")
    public AnswerDTO getAnswer(@RequestBody QuestionDTO question) {
        return chatService.getAnswer(question);
    }
}
