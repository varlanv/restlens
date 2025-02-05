package com.varlanv.impl;

import com.varlanv.BaseTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonMapTest extends BaseTest {

    @Nested
    class PutString {

        @Test
        void should_decline_null_keys() {
            assertThatThrownBy(() -> new JsonMap().put(null, "any")).isInstanceOf(NullPointerException.class);
        }

        @Test
        void should_decline_null_values() {
            assertThatThrownBy(() -> new JsonMap().put("any", (String) null)).isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class ToJson {

        @Test
        void should_return_empty_json_for_empty_map() {
            assertThat(new JsonMap().toJson()).isEqualTo("{}");
        }

        @Test
        void should_parse_string_map() {
            var subject = new JsonMap().put("key", "value");
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": "value"
                }"""
            );
        }

        @Test
        void should_parse_integer_map() {
            var subject = new JsonMap().put("key", 123);
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": 123
                }"""
            );
        }

        @Test
        void should_parse_string_and_integer_map() {
            var subject = new JsonMap().put("stringKey", "value").put("integerKey", 123);
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "stringKey": "value",
                  "integerKey": 123
                }"""
            );
        }

        @Test
        void should_parse_bigdecimal_map() {
            var subject = new JsonMap().put("key", new BigDecimal("12.34"));
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": 12.34
                }"""
            );
        }

        @Test
        void should_parse_bigdecimal_list_map() {
            var subject = new JsonMap().putMany("key", new BigDecimal("12.34"), new BigDecimal("56.789"));
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": [12.34, 56.789]
                }"""
            );
        }

        @Test
        void should_parse_string_list_map() {
            var subject = new JsonMap().putMany("key", "val1", "val2", "val3");
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": ["val1", "val2", "val3"]
                }"""
            );
        }

        @Test
        void should_parse_empty_string_list_map() {
            var strings = new String[0];
            var subject = new JsonMap().putMany("key", strings);
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": []
                }"""
            );
        }

        @Test
        void should_parse_empty_string_list_and_integer_map() {
            var strings = new String[0];
            var subject = new JsonMap().putMany("key", strings).put("integerKey", 123);
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "key": [],
                  "integerKey": 123
                }"""
            );
        }

        @Test
        void should_parse_empty_string_list_and_empty_integer_list_map() {
            var strings = new String[0];
            var integers = new Integer[0];
            var subject = new JsonMap().putMany("strings", strings).putMany("integers", integers);
            assertThat(subject.toJson()).isEqualTo("""
                {
                  "strings": [],
                  "integers": []
                }"""
            );
        }
    }
}
