package com.sfb.demos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Path("/demo")
public class ResteasyResource {
    private final ObjectMapper mapper;
    private final Map<String, DemoDto> demoResources;

    @Inject
    public ResteasyResource(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        this.demoResources = Files
                .list(Paths.get("../resources"))
                .filter(path -> path.toString().endsWith(".json"))
                .collect(toMap(path -> path.getFileName().toString(), this::parse));
    }

    @GET
    @Path("{resource}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("resource") String resource) {
        return Optional.ofNullable(demoResources.get(resource))
                .map(resourceDto -> Response.ok(resourceDto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Getter
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    public static class DemoDto {
        private final String status;
        private final String name;
        private final String period;
        private final String unit;
        private final String description;
        private final List<DemoValue> values;

    }

    @Getter
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    public static class DemoValue {
        private final BigDecimal x;
        private final BigDecimal y;

        public BigDecimal getX() {
            return x;
        }

        public BigDecimal getY() {
            return y;
        }
    }

    private DemoDto parse(java.nio.file.Path path) {
        try {
            return mapper.readValue(path.toFile(), DemoDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
