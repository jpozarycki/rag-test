package com.jpozarycki.ragtest.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetPromptAnswerResponseDTO(@JsonProperty @JsonPropertyDescription("Step-by-step reasoning for generating answer") String reasoning,
                                         @JsonProperty @JsonPropertyDescription("Generated answer") String answer) {
}
