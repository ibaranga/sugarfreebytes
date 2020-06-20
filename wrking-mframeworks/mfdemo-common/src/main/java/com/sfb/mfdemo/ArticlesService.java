package com.sfb.mfdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ArticlesService {
    private final Map<String, Articles> demoArticles;

    public ArticlesService(Map<String, Articles> demoArticles) {
        this.demoArticles = Map.copyOf(demoArticles);
    }

    public Articles getArticles(String path) {
        return demoArticles.get(path);
    }

    public static ArticlesService forLocation(Path resourcesPath) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Map<String, Articles> articles = Files.list(resourcesPath)
                .filter(path -> path.toString().endsWith(".json"))
                .collect(toMap(path -> path.getFileName().toString(), path -> {
                    try {
                        return mapper.readValue(path.toFile(), Articles.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));

        return new ArticlesService(articles);
    }

}
