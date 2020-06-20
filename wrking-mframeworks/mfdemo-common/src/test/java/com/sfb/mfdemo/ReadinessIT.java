package com.sfb.mfdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.nio.file.Paths;

@EnabledIfSystemProperty(named = "app", matches = "[a-z]+")
public class ReadinessIT {
    Client client = ClientBuilder.newClient().register(new ContextResolver<ObjectMapper>() {
        @Override
        public ObjectMapper getContext(Class<?> type) {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
    });

    ArticlesService articlesService = ArticlesService.forLocation(Paths.get("../resources"));

    public ReadinessIT() throws IOException {
    }

    @AfterEach
    public void tearDown() {
        client.close();
    }

    @Test
    @EnabledIfSystemProperty(named = "app", matches = "quarkus-resteasy")
    public void testQuarkusResteasy() {
        testReadiness("http://localhost:18080/quarkus-resteasy/md.json", articlesService.getArticles("md.json"));
    }

    @Test
    @EnabledIfSystemProperty(named = "app", matches = "quarkus-vertx-web")
    public void testQuarkusVertxWeb() {
        testReadiness("http://localhost:18080/quarkus-vertx-web/md.json", articlesService.getArticles("md.json"));
    }

    @Test
    @EnabledIfSystemProperty(named = "app", matches = "spring-boot")
    public void testSpringBoot() {
        testReadiness("http://localhost:18080/spring-boot/md.json", articlesService.getArticles("md.json"));
    }

    @Test
    @EnabledIfSystemProperty(named = "app", matches = "spring-boot-webflux")
    public void testSpringBootWebflux() {
        testReadiness("http://localhost:18080/spring-boot-webflux/md.json", articlesService.getArticles("md.json"));
    }

    @Test
    @EnabledIfSystemProperty(named = "app", matches = "micronaut")
    public void testMicronaut() {
        testReadiness("http://localhost:18080/micronaut/md.json", articlesService.getArticles("md.json"));

    }

    @Test
    @EnabledIfSystemProperty(named = "app", matches = "micronaut-reactive")
    public void testMicronautReactive() {
        testReadiness("http://localhost:18080/micronaut-reactive/md.json", articlesService.getArticles("md.json"));
    }

    void testReadiness(String url, Articles articles) {
        assertGetResponse(url, articles);
        assertPostResponse(url, articles, articles);
    }

    void assertGetResponse(String url, Articles expected) {
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(expected, response.readEntity(Articles.class));
    }

    void assertPostResponse(String url, Articles sent, Articles expected) {
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(sent));
        Assertions.assertEquals(200, response.getStatus(), () -> response.readEntity(String.class));
        Assertions.assertEquals(expected, response.readEntity(Articles.class));
    }
}
