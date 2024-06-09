package com.jpozarycki.ragtest.upload.services;

import com.jpozarycki.ragtest.upload.model.PostUploadRequestDTO;
import com.jpozarycki.ragtest.upload.model.PostUploadResponseDTO;

public interface UploadService {
    PostUploadResponseDTO uploadToVectorStore(PostUploadRequestDTO postUploadData);
}
