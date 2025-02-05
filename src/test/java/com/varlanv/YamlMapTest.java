package com.varlanv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class YamlMapTest extends BaseTest {

    @Nested
    class ToYaml {

        @Test
        void should_return_empty_for_empty_map() {
            assertThat(new YamlMap().toYaml()).isEmpty();
        }

        @Test
        void one_string_key__should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap().put("key", "val")
                )
            ).isEqualTo(""
                + "key: val\n"
            );
        }

        @Test
        void one_int_key__should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap().put("key", 1)
                )
            ).isEqualTo("key: 1\n");
        }

        @Test
        void one_int_and_one_string_key__should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap()
                        .put("intKey", 1)
                        .put("stringKey", "a"))
            ).isEqualTo(""
                + "intKey: 1\n"
                + "stringKey: a\n"
            );
        }

        @Test
        void one_bigdecimal_key__should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap()
                        .put("key", Double.parseDouble("12.34")))
            ).isEqualTo(""
                + "key: 12.34\n"
            );
        }

        @Test
        void string_list_key_should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap()
                        .putMany("stringKey", "a", "b", "c")
                )
            ).isEqualTo(""
                + "stringKey:\n"
                + "  - a\n"
                + "  - b\n"
                + "  - c\n"
            );
        }

        @Test
        void int_list_key_should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap()
                        .putMany("intKey", 1, 2, 3))
            ).isEqualTo(""
                + "intKey:\n"
                + "  - 1\n"
                + "  - 2\n"
                + "  - 3\n"
            );
        }

        @Test
        void string_list_and_int_list_should_return_correct_yaml() {
            assertThat(
                toYaml(
                    new YamlMap()
                        .putMany("stringKey", "a", "b", "c")
                        .putMany("intKey", 4, 5, 6)
                )
            ).isEqualTo(""
                + "stringKey:\n"
                + "  - a\n"
                + "  - b\n"
                + "  - c\n"
                + "intKey:\n"
                + "  - 4\n"
                + "  - 5\n"
                + "  - 6\n"
            );
        }

        @Test
        void one_level_nested_map_should_return_correct_yaml() {
            var actual = toYaml(
                new YamlMap()
                    .put("intKey", 1)
                    .putNested("nestedKey", Map.of("stringKey", "a"))
            );

            assertThat(actual).isEqualTo(""
                + "intKey: 1\n"
                + "nestedKey:\n"
                + "  stringKey: a\n"
            );
        }

        @Test
        void one_level_nested_map_with_many_keys_should_return_correct_yaml() {
            var nested = new LinkedHashMap<String, String>();
            nested.put("stringKey1", "a");
            nested.put("stringKey2", "b");
            nested.put("stringKey3", "c");
            nested.put("stringKey4", "d");
            var actual = toYaml(
                new YamlMap()
                    .put("intKey", 1)
                    .putNested("nestedKey", nested)
            );
            assertThat(actual).isEqualTo(""
                + "intKey: 1\n"
                + "nestedKey:\n"
                + "  stringKey1: a\n"
                + "  stringKey2: b\n"
                + "  stringKey3: c\n"
                + "  stringKey4: d\n"
            );
        }

        @Test
        void one_level_nested_map_with_list_value_should_return_correct_yaml() {
            var actual = toYaml(
                new YamlMap()
                    .put("intKey", 1)
                    .putNested("nestedKey", Map.of("nestedListKey", List.of("a", "b", "c")))
            );

            assertThat(actual).isEqualTo("" +
                "intKey: 1\n"
                + "nestedKey:\n"
                + "  nestedListKey:\n"
                + "    - a\n"
                + "    - b\n"
                + "    - c\n"
            );
        }

        @Test
        void two_level_nested_map_with_list_value_should_return_correct_yaml() {
            var actual = toYaml(
                new YamlMap()
                    .put("intKey", 1)
                    .putNested("nested1", Map.of("nested2", Map.of("nestedListKey", List.of("a", "b", "c"))))
            );

            assertThat(actual).isEqualTo(""
                + "intKey: 1\n"
                + "nested1:\n"
                + "  nested2:\n"
                + "    nestedListKey:\n"
                + "      - a\n"
                + "      - b\n"
                + "      - c\n"
            );
        }

        @Test
        void three_level_nested_map_with_two_list_values_at_different_nest_levels_should_return_correct_yaml() {
            var nested1 = new LinkedHashMap<String, String>();
            nested1.put("stringKey1", "a");
            nested1.put("stringKey2", "b");
            nested1.put("stringKey3", "c");
            nested1.put("stringKey4", "d");

            var nested2 = List.of(5, 6, 7, 8);

            var nestedMap = new LinkedHashMap<String, Object>();
            nestedMap.put("nestedKey1", nested1);
            nestedMap.put("nestedKey2", Map.of("nestedKey3", nested2));
            var actual = toYaml(
                new YamlMap()
                    .put("intKey", 1)
                    .putNested("nested", nestedMap)
            );
            assertThat(actual).isEqualTo(""
                + "intKey: 1\n"
                + "nested:\n"
                + "  nestedKey1:\n"
                + "    stringKey1: a\n"
                + "    stringKey2: b\n"
                + "    stringKey3: c\n"
                + "    stringKey4: d\n"
                + "  nestedKey2:\n"
                + "    nestedKey3:\n"
                + "      - 5\n"
                + "      - 6\n"
                + "      - 7\n"
                + "      - 8\n"
            );
        }
    }

    private String toYaml(YamlMap yaml) {
        var res = yaml.toYaml();
        assertThatNoException()
            .as(yaml.toString())
            .isThrownBy(() -> {
                @SuppressWarnings("unchecked")
                var jacksonMap = (Map<String, Object>) new ObjectMapper(new YAMLFactory()).readValue(res, Map.class);
                assertThat(yaml.state).containsAllEntriesOf(jacksonMap);
            });
        return res;
    }
}
