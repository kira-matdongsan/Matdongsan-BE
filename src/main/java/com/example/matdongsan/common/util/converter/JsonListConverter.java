package com.example.matdongsan.common.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class JsonListConverter implements AttributeConverter<List<Integer>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("List → JSON 변환 실패", e);
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(
                    dbData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class)
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON → List 변환 실패", e);
        }
    }
}