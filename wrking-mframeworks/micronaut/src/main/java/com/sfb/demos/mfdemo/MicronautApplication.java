package com.sfb.demos.mfdemo;

import com.sfb.mfdemo.Articles;
import com.sfb.mfdemo.ArticlesService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.runtime.Micronaut;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Controller
public class MicronautApplication {
    private final ArticlesService articlesService;

    @Inject
    public MicronautApplication(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @Get(uri = "/micronaut/{path}")
    public HttpResponse<?> getArticles(@PathVariable("path") String path) {
        return ofNullable(articlesService.getArticles(path)).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }

    @Post(uri = "/micronaut/{path}")
    public HttpResponse<?> saveArticles(@PathVariable("path") String path, @Body Articles articles) {
        return HttpResponse.ok(articles);
    }

    public static void main(String[] args) {
        Micronaut.run(MicronautApplication.class);
    }
}