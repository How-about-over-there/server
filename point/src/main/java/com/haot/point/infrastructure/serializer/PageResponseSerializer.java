package com.haot.point.infrastructure.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import org.springframework.data.redis.serializer.RedisSerializer;

public class PageResponseSerializer implements RedisSerializer<PageResponse<PointHistoryResponse>> {
    private final ObjectMapper objectMapper;

    public PageResponseSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(PageResponse<PointHistoryResponse> pageResponse) {
        try {
            return objectMapper.writeValueAsBytes(pageResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize PageResponse", e);
        }
    }

    @Override
    public PageResponse<PointHistoryResponse> deserialize(byte[] bytes) {
        try {
            if (bytes == null || bytes.length == 0) return null;
            return objectMapper.readValue(bytes, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize PageResponse", e);
        }
    }
}
