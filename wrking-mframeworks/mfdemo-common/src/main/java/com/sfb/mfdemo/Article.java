package com.sfb.mfdemo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@EqualsAndHashCode
public class Article {
    private final String slug;
    private final String title;
    private final String description;
    private final String body;
    private final List<String> tagList;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final boolean favorited;
    private final int favoritesCount;
    private final Author author;
}
