package com.sfb.mfdemo;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Paths;

@Singleton
public class VertxWebConfiguration {

    @PostConstruct
    public void init() {
        DatabindCodec.mapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DatabindCodec.prettyMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Produces
    @Singleton
    public ArticlesService articlesService() throws IOException {
        return ArticlesService.forLocation(Paths.get("../resources"));
    }
}
