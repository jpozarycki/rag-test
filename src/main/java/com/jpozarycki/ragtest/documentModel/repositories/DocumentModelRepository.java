package com.jpozarycki.ragtest.documentModel.repositories;

import com.jpozarycki.ragtest.documentModel.model.DocumentModel;
import org.springframework.data.repository.CrudRepository;

public interface DocumentModelRepository extends CrudRepository<DocumentModel, String> {
}
