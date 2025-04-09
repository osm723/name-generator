package com.oh.name_generator.name.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNameRequestDto {

    private String createName;

    @QueryProjection
    public CreateNameRequestDto(String createName) {
        this.createName = createName;
    }
}
