package com.sfb.demos.mfdemo;

import com.sfb.mfdemo.ArticlesService;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Configuration;
import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.context.env.ConfigurationAdvice;

import java.io.IOException;
import java.nio.file.Paths;

@Factory
public class MicronautConfiguration {
    @Bean
    public ArticlesService articlesService() throws IOException {
        return ArticlesService.forLocation(Paths.get("../resources"));
    }
}
