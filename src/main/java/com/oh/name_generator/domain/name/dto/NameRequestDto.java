package com.oh.name_generator.domain.name.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.oh.name_generator.global.common.consts.Constants.Validation.MAX_NAME_LENGTH;

@Getter
@Setter
public class NameRequestDto {

    @Size(max = MAX_NAME_LENGTH, message = "성은 최대 {max}자까지 가능합니다.")
    @Pattern(regexp = "^[가-힣]*$", message = "성은 한글만 입력 가능합니다.")
    private String lastName;

    @Size(max = MAX_NAME_LENGTH, message = "이름은 최대 {max}자까지 가능합니다.")
    @Pattern(regexp = "^[가-힣]*$", message = "이름은 한글만 입력 가능합니다.")
    private String firstName;

    @Size(max = MAX_NAME_LENGTH, message = "이름은 최대 {max}자까지 가능합니다.")
    @Pattern(regexp = "^[가-힣]*$", message = "이름은 한글만 입력 가능합니다.")
    private String secondName;

    @Pattern(regexp = "^[MWC]?$", message = "성별은 M(남아), W(여아), C(공통) 중 하나여야 합니다.")
    private String gender;

    private String selectedName;

    private Boolean useGpt = false;
}
