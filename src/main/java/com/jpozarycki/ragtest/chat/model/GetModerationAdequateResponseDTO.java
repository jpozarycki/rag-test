package com.jpozarycki.ragtest.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpozarycki.ragtest.chat.enums.ModerationAdequate;

public record GetModerationAdequateResponseDTO(@JsonProperty ModerationAdequate adequate) {
}
