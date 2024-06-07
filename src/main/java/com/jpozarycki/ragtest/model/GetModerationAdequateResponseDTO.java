package com.jpozarycki.ragtest.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.jpozarycki.ragtest.enums.ModerationAdequate;

public record GetModerationAdequateResponseDTO(@JsonPropertyDescription("Is prompt adequate") ModerationAdequate adequate) {
}
