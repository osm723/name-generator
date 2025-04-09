package com.oh.name_generator.name.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameRequestDto {

    @Size(max = 2, message = "성은 최대 2자까지 가능합니다.")
    private String lastName;

    @Size(max = 2, message = "이름은 최대 2자까지 가능합니다.")
    private String firstName;

    @Size(max = 2, message = "이름은 최대 2자까지 가능합니다.")
    private String secondName;

    @Size(max = 1)
    private String gender;

    private String selectedName;

    private Boolean useGpt;
}
