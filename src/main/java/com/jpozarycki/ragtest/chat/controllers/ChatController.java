package com.jpozarycki.ragtest.chat.controllers;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.model.QuestionDTO;
import com.jpozarycki.ragtest.chat.service.ChatService;
import com.jpozarycki.ragtest.documentModel.service.DocumentModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;
    private final DocumentModelService documentModelService;

    @PostMapping("/question")
    public ResponseEntity<AnswerDTO> getAnswer(@RequestBody QuestionDTO question) {
        if (!documentModelService.isDocumentModelExist(question.documentId())) {
            return ResponseEntity.notFound().build();
        }
        AnswerDTO answerDTO = chatService.getAnswer(question.question());
        return ResponseEntity.ok(answerDTO);
    }
}
