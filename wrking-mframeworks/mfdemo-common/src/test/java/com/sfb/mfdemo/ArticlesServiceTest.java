package com.sfb.mfdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

class ArticlesServiceTest {
    ArticlesService articlesService = ArticlesService.forLocation(Paths.get("../resources"));

    ArticlesServiceTest() throws IOException {
    }

    @Test
    void getArticles() {
        Assertions.assertNotNull(articlesService.getArticles("xs.json"));
    }
}