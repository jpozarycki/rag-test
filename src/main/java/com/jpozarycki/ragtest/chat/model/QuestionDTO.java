package com.jpozarycki.ragtest.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QuestionDTO(@JsonProperty String documentId, @JsonProperty String question) {
}
