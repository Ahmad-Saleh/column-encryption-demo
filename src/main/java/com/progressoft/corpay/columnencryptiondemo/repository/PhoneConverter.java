package com.progressoft.corpay.columnencryptiondemo.repository;

import jakarta.persistence.AttributeConverter;

public class PhoneConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return "079" + attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData.substring("079".length());
    }
}
