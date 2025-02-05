package com.varlanv.impl;

import org.jetbrains.annotations.NotNullByDefault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@NotNullByDefault
public class Specifications {

    History history = new History();

    public RequestBody.Steps.ResultStep requestBody(Function<RequestBody.Steps.Start, RequestBody.Steps.ResultStep> stepFunction) {
        return stepFunction.apply(new RequestBody.Steps.Start());
    }

    public static class RequestBody {

        public static class Steps {

            public static class Start {

                public OpIdStep withHttpGetPath(String path) {
                    return new OpIdStep(HttpMethod.GET, path);
                }

                public OpIdStep withHttpPostPath(String path) {
                    return new OpIdStep(HttpMethod.POST, path);
                }

                public OpIdStep withHttpDeletePath(String path) {
                    return new OpIdStep(HttpMethod.DELETE, path);
                }

                public OpIdStep withHttpPatchPath(String path) {
                    return new OpIdStep(HttpMethod.PATCH, path);
                }

                public OpIdStep withHttpOptionsPath(String path) {
                    return new OpIdStep(HttpMethod.OPTIONS, path);
                }

                public OpIdStep withHttpTracePath(String path) {
                    return new OpIdStep(HttpMethod.TRACE, path);
                }
            }

            public static class OpIdStep {

                final HttpMethod httpMethod;
                final String path;

                public OpIdStep(HttpMethod httpMethod, String path) {
                    this.httpMethod = httpMethod;
                    this.path = path;
                }

                public DescStep withOperationId(String operationId) {
                    return new DescStep(operationId, this);
                }

                public DescStep withoutOperationId() {
                    return new DescStep("", this);
                }
            }

            public static class DescStep {

                final String operationId;
                final OpIdStep parent;

                public DescStep(String operationId, OpIdStep parent) {
                    this.operationId = operationId;
                    this.parent = parent;
                }

                public FieldsStep describedAs(String summary) {
                    return this.describedAs(summary, "");
                }

                public FieldsStep describedAs(String summary, String description) {
                    return new FieldsStep(summary, description, this);
                }
            }

            public static class FieldsStep {

                final String summary;
                final String description;
                final DescStep parent;

                public FieldsStep(String summary, String description, DescStep parent) {
                    this.summary = summary;
                    this.description = description;
                    this.parent = parent;
                }

                public ResultStep withBodyFields(Function<RequestBodyFieldsSpec, RequestBodyFieldsSpec> bodyFieldsSpec) {
                    var fields = bodyFieldsSpec.apply(new RequestBodyFieldsSpec(false));
                    return new ResultStep(fields, this);
                }

                public ResultStep withOptionalBodyFields(Function<RequestBodyFieldsSpec, RequestBodyFieldsSpec> bodyFieldsSpec) {
                    var fields = bodyFieldsSpec.apply(new RequestBodyFieldsSpec(true));
                    return new ResultStep(fields, this);
                }
            }

            public static class ResultStep {

                final RequestBodyFieldsSpec fields;
                final FieldsStep parent;

                public ResultStep(RequestBodyFieldsSpec fields, FieldsStep parent) {
                    this.fields = fields;
                    this.parent = parent;
                }

                public <T> Supplier<T> mappedToObject(Class<T> type, ThrowingBiFunction<Map<String, Object>, Class<T>, T> function) {
                    return () -> {
                        try {
                            return function.apply(mapToMap(), type);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }

                public Supplier<LinkedHashMap<String, Object>> mappedToMap() {
                    return this::mapToMap;
                }

                private LinkedHashMap<String, Object> mapToMap() {
                    var result = new LinkedHashMap<String, Object>();
                    if (!fields.optional && fields.fields.isEmpty()) {
                        throw new OpenApiException("Empty fields - fail");
                    } else if (fields.optional && fields.fields.isEmpty()) {
                        return result;
                    } else {
                        for (var field : fields.fields) {
                            var fieldParts = field.path.split("\\.");
                            if (fieldParts.length == 1) {
                                result.put(fieldParts[0], field.exampleValue);
                            }
                        }
                        return result;
                    }

                }
            }
        }
    }

    public static class OpenApiField<T> {

        final String path;
        final Boolean required;
        final OpenApiFieldType openApiType;
        final T exampleValue;
        final Maybe<T> defaultValue;
        final Map<String, String> options;

        public OpenApiField(String path,
                            Boolean required,
                            OpenApiFieldType fieldType,
                            T exampleValue,
                            Maybe<T> defaultValue,
                            Map<String, String> options) {
            this.path = path;
            this.required = required;
            this.openApiType = fieldType;
            this.exampleValue = exampleValue;
            this.defaultValue = defaultValue;
            this.options = options;
        }
    }

    public static class RequestBodyFieldsSpec {

        final Boolean optional;
        final List<OpenApiField<?>> fields;

        public RequestBodyFieldsSpec(Boolean optional) {
            this(optional, Collections.emptyList());
        }

        RequestBodyFieldsSpec(Boolean optional, List<OpenApiField<?>> fields) {
            this.optional = optional;
            this.fields = fields;
        }

        public RequestBodyFieldsSpec requiredInt(String path, Function<IntegerSpecStart, IntegerSpecEnd> function) {
            var intSpec = function.apply(new IntegerSpecStart());
            return new RequestBodyFieldsSpec(
                this.optional,
                CollectionUtil.concatToListFinal(
                    fields,
                    intSpec.toOpenApiField(path, true)
                )
            );
        }

        public RequestBodyFieldsSpec optionalInt(String path, Function<IntegerSpecStart, IntegerSpecEnd> function) {
            var intSpec = function.apply(new IntegerSpecStart());
            return new RequestBodyFieldsSpec(
                this.optional,
                CollectionUtil.concatToListFinal(
                    fields,
                    intSpec.toOpenApiField(path, false)
                )
            );
        }
    }

    public static class IntegerSpecStart {

        public IntegerSpecEnd withExampleValue(Integer value) {
            return new IntegerSpecEnd(value);
        }
    }

    public static class IntegerSpecEnd {

        private final Integer exampleValue;
        private final String description;
        private final Maybe<Integer> defaultValue;
        private final Maybe<Integer> minimumExclusive;
        private final Maybe<Integer> minimumInclusive;
        private final Maybe<Integer> maximumExclusive;
        private final Maybe<Integer> maximumInclusive;
        private final Maybe<Integer> multipleOf;

        public IntegerSpecEnd(Integer exampleValue) {
            this.exampleValue = exampleValue;
            this.description = "";
            this.defaultValue = Maybe.empty();
            this.minimumExclusive = Maybe.empty();
            this.minimumInclusive = Maybe.empty();
            this.maximumExclusive = Maybe.empty();
            this.maximumInclusive = Maybe.empty();
            this.multipleOf = Maybe.empty();
        }

        public IntegerSpecEnd(Integer exampleValue,
                              String description,
                              Maybe<Integer> defaultValue,
                              Maybe<Integer> minimumExclusive,
                              Maybe<Integer> minimumInclusive,
                              Maybe<Integer> maximumExclusive,
                              Maybe<Integer> maximumInclusive,
                              Maybe<Integer> multipleOf) {
            this.exampleValue = exampleValue;
            this.description = description;
            this.defaultValue = defaultValue;
            this.minimumExclusive = minimumExclusive;
            this.minimumInclusive = minimumInclusive;
            this.maximumExclusive = maximumExclusive;
            this.maximumInclusive = maximumInclusive;
            this.multipleOf = multipleOf;
        }

        OpenApiField<Integer> toOpenApiField(String path, Boolean required) {
            var options = new LinkedHashMap<String, String>();
            this.maximumExclusive.ifPresent(v -> {
                options.put("maximum", v.toString());
                options.put("exclusiveMinimum", "true");
            });
            this.maximumInclusive.ifPresent(v -> options.put("maximum", v.toString()));
            this.minimumExclusive.ifPresent(v -> {
                options.put("minimum", v.toString());
                options.put("exclusiveMaximum", "true");
            });
            this.minimumInclusive.ifPresent(v -> options.put("minimum", v.toString()));
            this.multipleOf.ifPresent(v -> options.put("multipleOf", v.toString()));
            return new OpenApiField<>(
                path,
                required,
                OpenApiFieldType.INT32,
                this.exampleValue,
                this.defaultValue,
                Collections.unmodifiableMap(options)
            );
        }

        public IntegerSpecEnd describedAs(String description) {
            if (description.isBlank()) {
                throw new OpenApiException("Fail");
            }
            return new IntegerSpecEnd(
                this.exampleValue,
                description,
                this.defaultValue,
                this.minimumExclusive,
                this.minimumInclusive,
                this.maximumExclusive,
                this.maximumInclusive,
                this.multipleOf
            );
        }

        public IntegerSpecEnd minimumExclusive(Integer value) {
            if (this.exampleValue < value) {
                throw new OpenApiException("Fail");
            }
            return new IntegerSpecEnd(
                this.exampleValue,
                this.description,
                this.defaultValue,
                Maybe.of(value),
                this.minimumInclusive,
                this.maximumExclusive,
                this.maximumInclusive,
                this.multipleOf
            );
        }

        public IntegerSpecEnd minimumInclusive(Integer value) {
            if (this.exampleValue <= value) {
                throw new OpenApiException("Fail");
            }
            return new IntegerSpecEnd(
                this.exampleValue,
                this.description,
                this.minimumExclusive,
                this.defaultValue,
                Maybe.of(value),
                this.maximumExclusive,
                this.maximumInclusive,
                this.multipleOf
            );
        }

        public IntegerSpecEnd maximumExclusive(Integer value) {
            if (this.exampleValue > value) {
                throw new OpenApiException("Fail");
            }
            return new IntegerSpecEnd(
                this.exampleValue,
                this.description,
                this.defaultValue,
                this.minimumExclusive,
                this.minimumInclusive,
                Maybe.of(value),
                this.maximumInclusive,
                this.multipleOf
            );
        }

        public IntegerSpecEnd maximumInclusive(Integer value) {
            if (this.exampleValue >= value) {
                throw new OpenApiException("Fail");
            }
            return new IntegerSpecEnd(
                this.exampleValue,
                this.description,
                this.defaultValue,
                this.minimumExclusive,
                this.minimumInclusive,
                this.maximumExclusive,
                Maybe.of(value),
                this.multipleOf
            );
        }

        public IntegerSpecEnd multipleOf(Integer value) {
            if (exampleValue % value != 0) {
                throw new OpenApiException("Failing early because of mismatch in OpenAPI specification: example value [%s] is not multiple of [%s]", exampleValue, value);
            }
            return new IntegerSpecEnd(
                this.exampleValue,
                this.description,
                this.defaultValue,
                this.minimumExclusive,
                this.minimumInclusive,
                this.maximumExclusive,
                this.maximumInclusive,
                Maybe.of(value)
            );
        }
    }
}
