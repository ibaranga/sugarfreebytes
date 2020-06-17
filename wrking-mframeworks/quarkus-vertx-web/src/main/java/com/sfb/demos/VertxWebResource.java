package com.sfb.demos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.launcher.shaded.org.apache.http.HttpStatus;
import io.quarkus.vertx.web.Route;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class VertxWebResource {
    private final ObjectMapper mapper;
    private final Map<String, Article> demoResources;


    @Inject
    public VertxWebResource(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        this.demoResources = Files
                .list(Paths.get("../resources"))
                .filter(path -> path.toString().endsWith(".json"))
                .collect(toMap(path -> path.getFileName().toString(), this::parse));
    }


    @Route(path = "demo/:resource")
    void getResource(RoutingContext rc) {
        Optional.ofNullable(demoResources.get(rc.pathParam("resource")))
                .ifPresentOrElse(resource -> rc.response().end(Json.encode(resource)),
                        () -> rc.response().setStatusCode(HttpStatus.SC_NOT_FOUND).end());
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
