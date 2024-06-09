package com.jpozarycki.ragtest.common.repositories;

import com.jpozarycki.ragtest.common.model.DocumentModel;
import org.springframework.data.repository.CrudRepository;

public interface DocumentModelRepository extends CrudRepository<DocumentModel, String> {
}
