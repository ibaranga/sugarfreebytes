package com.sfb.mfdemo;

import io.quarkus.launcher.shaded.org.apache.http.HttpStatus;
import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
public class VertxWebResource {
    private final ArticlesService articlesService;

    @Inject
    public VertxWebResource(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @Route(path = "quarkus-vertx-web/:path", methods = HttpMethod.GET)
    void getArticles(RoutingContext rc) {
        ofNullable(articlesService.getArticles(rc.pathParam("path"))).map(Json::encode).ifPresentOrElse(
                body -> rc.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(body),
                () -> rc.response().setStatusCode(HttpStatus.SC_NOT_FOUND).end()
        );
    }

    @Route(path = "quarkus-vertx-web/:path", methods = HttpMethod.POST)
    void saveArticles(RoutingContext rc) {
        Articles articles = rc.getBodyAsJson().mapTo(Articles.class);
        rc.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(Json.encode(articles));
    }
}
