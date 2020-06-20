package com.sfb.mfdemo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@EqualsAndHashCode
public class Author {
    private final String username;
    private final String bio;
    private final String image;
    private final boolean following;
}
