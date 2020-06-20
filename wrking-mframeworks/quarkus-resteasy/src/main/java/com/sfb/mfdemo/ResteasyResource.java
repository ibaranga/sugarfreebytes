package com.sfb.mfdemo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("/quarkus-resteasy/{path}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResteasyResource {

    private final ArticlesService articlesService;

    @Inject
    public ResteasyResource(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GET
    public Response getArticles(@PathParam("path") String resource) {
        return ofNullable(articlesService.getArticles(resource))
                .map(resourceDto -> ok(resourceDto).build())
                .orElseGet(() -> status(NOT_FOUND).build());
    }

    @POST
    public Response saveArticles(Articles articles) {
        return ok(articles).build();
    }

}
