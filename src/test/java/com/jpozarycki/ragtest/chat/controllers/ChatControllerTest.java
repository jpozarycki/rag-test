package com.jpozarycki.ragtest.chat.controllers;

import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.model.QuestionDTO;
import com.jpozarycki.ragtest.chat.service.ChatService;
import com.jpozarycki.ragtest.documentModel.service.DocumentModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatControllerTest {
    private static final String DOCUMENT_ID = "documentId";
    private static final String QUESTION = "What's in the box?";
    private static final String ANSWER = "Gummy bears!";

    private ChatController chatController;
    @Mock
    private ChatService chatService;

    @Mock
    private DocumentModelService documentModelService;

    @BeforeEach
    void setUp() {
        chatService = mock(ChatService.class);
        documentModelService = mock(DocumentModelService.class);
        chatController = new ChatController(chatService, documentModelService);
    }

    @Test
    void getAnswer_returnsOkOnDocumentIdFound() {
        QuestionDTO questionDTO = new QuestionDTO(DOCUMENT_ID, QUESTION);
        AnswerDTO answerDTO = new AnswerDTO(ANSWER);

        when(documentModelService.isDocumentModelExist(eq(DOCUMENT_ID))).thenReturn(true);
        when(chatService.getAnswer(eq(QUESTION))).thenReturn(answerDTO);


        ResponseEntity<AnswerDTO> responseDTO = chatController.getAnswer(questionDTO);

        assertEquals(HttpStatus.OK.value(), responseDTO.getStatusCode().value());
        assertNotNull(responseDTO.getBody());
        assertEquals(answerDTO, responseDTO.getBody());
    }

    @Test
    void getAnswer_returnsNotFoundOnDocumentIdNotFound() {
        QuestionDTO questionDTO = new QuestionDTO(DOCUMENT_ID, QUESTION);

        when(documentModelService.isDocumentModelExist(eq(DOCUMENT_ID))).thenReturn(false);

        ResponseEntity<AnswerDTO> responseDTO = chatController.getAnswer(questionDTO);

        assertEquals(HttpStatus.NOT_FOUND.value(), responseDTO.getStatusCode().value());
        verify(chatService, never()).getAnswer(anyString());
    }

    @Test
    void getAnswer_returnsBadRequestOnEmptyQuestion() {
        QuestionDTO questionDTO = new QuestionDTO(DOCUMENT_ID, "");

        ResponseEntity<AnswerDTO> responseDTO = chatController.getAnswer(questionDTO);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseDTO.getStatusCode().value());
        verify(chatService, never()).getAnswer(anyString());
    }

    @Test
    void getAnswer_returnsBadRequestOnNullQuestion() {
        QuestionDTO questionDTO = new QuestionDTO(DOCUMENT_ID, null);

        ResponseEntity<AnswerDTO> responseDTO = chatController.getAnswer(questionDTO);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseDTO.getStatusCode().value());
        verify(chatService, never()).getAnswer(anyString());
    }
}