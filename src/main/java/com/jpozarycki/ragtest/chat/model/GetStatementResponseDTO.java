package com.jpozarycki.ragtest.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetStatementResponseDTO(@JsonProperty @JsonPropertyDescription("Generated statement") String statement,
                                       @JsonProperty @JsonPropertyDescription("Step-by-step reasoning for generating statement") String reasoning) {
}
