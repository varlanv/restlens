package com.varlanv;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class YamlMapTest extends BaseTest {

    @Nested
    class ToYaml {

        @Test
        void should_return_empty_for_empty_map() {
            assertThat(new YamlMap().toYaml()).isEmpty();
        }

        @Test
        void one_string_key__should_return_correct_yaml() {
            assertThat(new YamlMap().put("key", "val").toYaml()).isEqualTo("key: val\n");
        }

        @Test
        void one_int_key__should_return_correct_yaml() {
            assertThat(new YamlMap().put("key", 1).toYaml()).isEqualTo("key: 1\n");
        }

        @Test
        void one_int_and_one_string_key__should_return_correct_yaml() {
            assertThat(new YamlMap().put("intKey", 1).put("stringKey", "2").toYaml()).isEqualTo("intKey: 1\nstringKey: 2\n");
        }

        @Test
        void one_bigdecimal_key__should_return_correct_yaml() {
            assertThat(new YamlMap().put("key", new BigDecimal("12.34")).toYaml()).isEqualTo("key: 12.34\n");
        }

        @Test
        void string_list_key_should_return_correct_yaml() {
            assertThat(new YamlMap().putMany("stringKey", "1", "2", "3").toYaml()).isEqualTo("stringKey:\n  - 1\n  - 2\n  - 3\n");
        }

        @Test
        void int_list_key_should_return_correct_yaml() {
            assertThat(new YamlMap().putMany("intKey", 1, 2, 3).toYaml()).isEqualTo("intKey:\n  - 1\n  - 2\n  - 3\n");
        }

        @Test
        void string_list_and_int_list_should_return_correct_yaml() {
            assertThat(new YamlMap().putMany("stringKey", "1", "2", "3").putMany("intKey", 4, 5, 6).toYaml())
                .isEqualTo("stringKey:\n  - 1\n  - 2\n  - 3\nintKey:\n  - 4\n  - 5\n  - 6\n");
        }
    }
}
