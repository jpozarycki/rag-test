package com.jpozarycki.ragtest.upload.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jpozarycki.ragtest.upload.enums.UploadStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostUploadResponseDTO(UploadStatus status, String documentId, String message) {
}
