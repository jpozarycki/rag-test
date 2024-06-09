package com.jpozarycki.ragtest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpozarycki.ragtest.chat.model.AnswerDTO;
import com.jpozarycki.ragtest.chat.model.QuestionDTO;
import com.jpozarycki.ragtest.upload.enums.UploadStatus;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UploadAndChatIntegrationTests {
    private static final byte[] FILE_CONTENT = "My name is James and I have a dog named Argos".getBytes();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUploadAndChat_Success() throws Exception {
        // Create a mock MultipartFile
        MockMultipartFile file = new MockMultipartFile("document", "test.txt", MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT);

        // Perform the POST request to the /upload endpoint
        MvcResult uploadResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response body
        String responseBody = uploadResult.getResponse().getContentAsString();
        PostUploadResponseDTO responseDTO = objectMapper.readValue(responseBody, PostUploadResponseDTO.class);

        // Assert the response
        assertEquals(UploadStatus.SUCCESS, responseDTO.status());
        assertNotNull(responseDTO.documentId());

        // Use the documentId from the response in the next test
        testGetAnswer(responseDTO.documentId());
    }

    @Test
    public void testUpload_BadRequest() throws Exception {
        // Create an empty mock MultipartFile
        MockMultipartFile file = new MockMultipartFile("document", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);

        // Perform the POST request to the /upload endpoint
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    private void testGetAnswer(String documentId) throws Exception {
        // Prepare the test data
        QuestionDTO questionDTO = new QuestionDTO(documentId, "What's the name of James' dog?");

        // Perform the POST request
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/question")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionDTO));

        // Execute the request and assert the response
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response body
        String responseBody = result.getResponse().getContentAsString();
        AnswerDTO answerDTO = objectMapper.readValue(responseBody, AnswerDTO.class);

        // Assert the response
        assertNotNull(answerDTO.answer());
        assertTrue(answerDTO.answer().contains("Argos"));
    }
}
