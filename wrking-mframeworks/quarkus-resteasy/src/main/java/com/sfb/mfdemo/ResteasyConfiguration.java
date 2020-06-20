package com.sfb.mfdemo;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Paths;

@Singleton
public class ResteasyConfiguration {

    @Produces
    @Singleton
    public ArticlesService articlesService() throws IOException {
        return ArticlesService.forLocation(Paths.get("../resources"));
    }
}
