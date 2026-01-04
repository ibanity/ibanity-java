package com.ibanity.apis.client.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class IbanityUtilsTest {

    @Test
    void objectMapper_shouldDeserializeJsonWithUnknownProperties() {
        ObjectMapper objectMapper = IbanityUtils.objectMapper();

        String jsonWithUnknownProperty = "{\"name\":\"John\",\"unknownField\":\"value\",\"anotherUnknown\":123}";

        TestPerson person = objectMapper.readValue(jsonWithUnknownProperty, TestPerson.class);

        assertThat(person.getName()).isEqualTo("John");
    }

    @Test
    void objectMapper_shouldExcludeNullPropertiesFromSerialization() {
        ObjectMapper objectMapper = IbanityUtils.objectMapper();

        TestPerson person = TestPerson.builder()
                .name("John")
                .email(null)
                .build();

        String json = objectMapper.writeValueAsString(person);

        assertThat(json).isEqualTo("{\"name\":\"John\"}");
    }

    @Test
    void objectMapper_shouldWriteDateAsTimestamp() {
        ObjectMapper objectMapper = IbanityUtils.objectMapper();

        Instant instant = Instant.parse("2023-01-15T10:30:00.000Z");
        Date date = Date.from(instant);

        TestWithDate testObject = TestWithDate.builder()
                .id("123")
                .createdAt(instant)
                .updatedAt(date)
                .build();

        String json = objectMapper.writeValueAsString(testObject);
        assertThat(json).isEqualTo("{\"createdAt\":\"2023-01-15T10:30:00Z\",\"id\":\"123\",\"updatedAt\":\"2023-01-15T10:30:00.000Z\"}");
    }

    @Test
    void objectMapper_shouldHandleEmptyObjectSerialization() {
        ObjectMapper objectMapper = IbanityUtils.objectMapper();

        TestPerson person = TestPerson.builder().build();

        String json = objectMapper.writeValueAsString(person);

        assertThat(json).isEqualTo("{}");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestPerson {
        private String name;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestWithDate {
        private String id;
        private Instant createdAt;
        private Date updatedAt;
    }
}
