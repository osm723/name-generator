package com.oh.name_generator.fortune.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FortuneRequestDto {

    private String inputWord;

    @Min(value = 1900_00_00, message = "생년월일은 1900년 01월 01일 이상이여야 합니다.")
    @Max(value = 2100_00_00, message = "생년월일은 2100년 01월 01일 이하이여야 합니다.")
    //@PastOrPresent(message = "")
    private Integer birth;

}
