package com.sfb.mfdemo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@EqualsAndHashCode
public class Articles {
    private final List<Article> articles;
    private final int articlesCount;

}

