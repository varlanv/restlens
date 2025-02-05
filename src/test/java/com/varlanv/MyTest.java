package com.varlanv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varlanv.impl.Specifications;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.assertj.core.api.Assertions.assertThat;

public class MyTest {

    ObjectMapper mapper = new ObjectMapper();


    @Test
    void qwe() {
        record SomeObj(Integer someOptInt, Integer someReqInt) {
        }
        var mapSupplier = new Specifications()
                .requestBody(spec -> spec
                        .withHttpGetPath("/qwe")
                        .withOperationId("opId")
                        .describedAs("summary")
                        .withOptionalBodyFields(fields -> fields
                                .optionalInt("someOptInt", someInt -> someInt
                                        .withExampleValue(20)
                                        .describedAs("Some cool int")
                                        .minimumExclusive(10)
                                        .maximumExclusive(50)
                                        .multipleOf(10)
                                )
                                .requiredInt("someReqInt", someInt -> someInt.withExampleValue(5))
                        )
                )
                .mappedToObject(SomeObj.class, mapper::convertValue);

        System.out.println(mapSupplier.get());
    }


    @Test
    void asd() throws Exception {
        WebHandler handler = (exchange) -> {
            System.out.println("In handler start");
            var voidMono = exchange.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("kek".getBytes(StandardCharsets.UTF_8))));
            System.out.println("In handler end");
            return voidMono.doFinally(s -> System.out.println("In handler finally"));
        };
        var build = WebTestClient.bindToWebHandler(handler)
                .webFilter((exchange, filterChain) -> {
                    System.out.println("Exchange request path -> " + exchange.getRequest().getPath());
                    System.out.println("Exchange request URI -> " + exchange.getRequest().getURI());
                    System.out.println("Before filter");
                    Mono<Void> filter = filterChain.filter(exchange);
                    System.out.println("After filter");
                    return filter.doFinally(s -> System.out.println("Finally filter"));
                })
                .build();

        build.delete().uri("http://localhost:8080/api/test").exchange()
                .expectBody(String.class)
                .consumeWith(r -> {
                    System.out.println("In consumer");
                    assertThat(r.getResponseBody()).isEqualTo("kek");
                });

        System.out.println("After spec");

        Files.writeString(Paths.get("openapi"), "keka1", StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }
}
