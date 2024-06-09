package com.jpozarycki.ragtest.common.model;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Document")
public record DocumentModel(String id, String fileName) {
}
