package com.sfb.mfdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/spring-boot/{path}")
@SpringBootApplication
public class SpringBootWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    private final ArticlesService articlesService;

    public SpringBootWebApplication(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping
    public ResponseEntity<?> getArticles(@PathVariable("path") String path) {
        return ofNullable(articlesService.getArticles(path))
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveArticles(@PathVariable("path") String path, @RequestBody Articles articles) {
        return ok(articles);
    }

}
