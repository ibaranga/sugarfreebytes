package com.sfb.mfdemo;

import com.sfb.mfdemo.ArticlesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public ArticlesService articlesService() throws IOException {
        return ArticlesService.forLocation(Paths.get("../resources"));
    }

}
