package com.jpozarycki.ragtest.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PostJinaRerankerResponseDTO(@JsonProperty String model,
                                          @JsonProperty Usage usage,
                                          @JsonProperty List<Result> results) {
    public record Usage(@JsonProperty("total_tokens") int totalTokens,
                        @JsonProperty("prompt_tokens") int prompt_tokens) {
    }

    public record Result(@JsonProperty int index,
                         @JsonProperty Document document,
                         @JsonProperty("relevance_score") double relevanceScore
    ) {
        public record Document(@JsonProperty String text) {
        }
    }
}
