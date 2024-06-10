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

import static com.jpozarycki.ragtest.helpers.Answers.EMPTY_QUESTION;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;
    private final DocumentModelService documentModelService;

    @PostMapping("/question")
    public ResponseEntity<AnswerDTO> getAnswer(@RequestBody QuestionDTO question) {
        if (question.question() == null || question.question().isEmpty()) {
            return ResponseEntity.badRequest().body(new AnswerDTO(EMPTY_QUESTION));
        }
        if (question.documentId() == null || !documentModelService.isDocumentModelExist(question.documentId())) {
            return ResponseEntity.notFound().build();
        }
        AnswerDTO answerDTO = chatService.getAnswer(question.question());
        return ResponseEntity.ok(answerDTO);
    }
}
