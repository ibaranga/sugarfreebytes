package com.sfb.demos.mfdemo;

import com.sfb.mfdemo.Articles;
import com.sfb.mfdemo.ArticlesService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.runtime.Micronaut;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Controller(value = "/micronaut-reactive/{path}")
public class MicronautReactiveApplication {
    private final ArticlesService articlesService;

    @Inject
    public MicronautReactiveApplication(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @Get
    public Maybe<Articles> getArticles(@PathVariable("path") String path) {
        return ofNullable(articlesService.getArticles(path)).map(Maybe::just).orElseGet(Maybe::empty);
    }

    @Post
    public Maybe<Articles> saveArticles(@PathVariable("path") String path, @Body Single<Articles> articles) {
        return articles.toMaybe();
    }

    public static void main(String[] args) {
        Micronaut.run(MicronautReactiveApplication.class);
    }
}