package com.jpozarycki.ragtest.upload.model;

import com.jpozarycki.ragtest.upload.enums.UploadStatus;

public record PostUploadResponseDTO(UploadStatus status, String documentId) {
}
