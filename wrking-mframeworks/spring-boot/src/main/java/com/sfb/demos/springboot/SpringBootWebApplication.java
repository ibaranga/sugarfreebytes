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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
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
    private final Map<String, Article> demoResources;

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
    public static class Article {
        private final String slug;
        private final String title;
        private final String description;
        private final String body;
        private final List<String> tagList;
        private final Instant createdAt;
        private final Instant updatedAt;
        private final boolean favorited;
        private final int favoritesCount;
        private final Author author;
    }

    @Getter
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    public static class Author {
        private final String username;
        private final String bio;
        private final String image;
        private final boolean following;
    }

    private Article parse(java.nio.file.Path path) {
        try {
            return mapper.readValue(path.toFile(), Article.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
