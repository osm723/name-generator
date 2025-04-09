package com.oh.name_generator.name.type;

import lombok.Getter;

@Getter
public enum Gender {
    M("남아"),
    W("여아"),
    C("공통");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

}
