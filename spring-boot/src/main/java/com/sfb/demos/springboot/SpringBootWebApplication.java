package com.sfb.demos.springboot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("demo")
@SpringBootApplication
public class SpringBootWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    private final ObjectMapper mapper;
    private final Map<String, DemoDto> demoResources;

    public SpringBootWebApplication(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        this.demoResources = Files
                .list(Paths.get("../resources"))
                .filter(path -> path.toString().endsWith(".json"))
                .collect(toMap(path -> path.getFileName().toString(), this::parse));
    }

    @GetMapping("/{resource}.json")
    public ResponseEntity<?> getDemoResource(@PathVariable("resource") String resource) {
        return ofNullable(demoResources.get(resource))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
