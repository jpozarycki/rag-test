package com.jpozarycki.ragtest.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PostJinaRerankerRequestDTO(@JsonProperty String model,
                                         @JsonProperty String query,
                                         @JsonProperty List<String> documents,
                                         @JsonProperty("top_n") int topN) {
}
