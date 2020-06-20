package com.sfb.mfdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Paths;

import static java.util.Optional.ofNullable;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class SpringBootWebfluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ArticlesService articlesService) {
        return route(GET("/spring-boot-webflux/{path}"), req ->
                ofNullable(articlesService.getArticles(req.pathVariable("path")))
                        .map(resource -> ok().body(Mono.just(resource), Articles.class))
                        .orElseGet(() -> notFound().build()))

                .and(route(POST("/spring-boot-webflux/{path}"), req ->
                        ok().body(req.bodyToMono(Articles.class), Articles.class)));
    }

    @Bean
    public ArticlesService articlesService() throws IOException {
        return ArticlesService.forLocation(Paths.get("../resources"));
    }
}
