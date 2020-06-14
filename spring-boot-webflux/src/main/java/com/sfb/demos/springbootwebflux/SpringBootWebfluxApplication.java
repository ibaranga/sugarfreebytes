package com.sfb.demos.springbootwebflux;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class SpringBootWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    private final ObjectMapper mapper;
    private final Map<String, DemoDto> demoResources;

    public SpringBootWebfluxApplication(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        this.demoResources = Files
                .list(Paths.get("../resources"))
                .filter(path -> path.toString().endsWith(".json"))
                .collect(toMap(path -> path.getFileName().toString(), this::parse));
    }

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/demo/{resource}"), req ->
                ofNullable(demoResources.get(req.pathVariable("resource")))
                        .map(resource -> ok().body(Mono.just(resource), DemoDto.class))
                        .orElseGet(() -> notFound().build())
        );
    }

    @Getter
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    public static class DemoDto {
        private final String status;
        private final String name;
        private final String period;
        private final String unit;
        private final String description;
        private final List<DemoValue> values;

    }

    @Getter
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    public static class DemoValue {
        private final BigDecimal x;
        private final BigDecimal y;

        public BigDecimal getX() {
            return x;
        }

        public BigDecimal getY() {
            return y;
        }
    }

    private DemoDto parse(Path path) {
        try {
            return mapper.readValue(path.toFile(), DemoDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
