package com.oh.name_generator.domain.fortune.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.oh.name_generator.global.common.consts.Constants.Validation.MAX_BIRTH_YEAR;
import static com.oh.name_generator.global.common.consts.Constants.Validation.MIN_BIRTH_YEAR;

@Getter
@Setter
@NoArgsConstructor
public class FortuneRequestDto {

    @NotBlank(message = "운세를 위한 단어를 입력해주세요.")
    @Size(max = 10, message = "단어는 최대 10자까지 입력 가능합니다.")
    private String inputWord;

    @Min(value = MIN_BIRTH_YEAR, message = "생년월일은 1900년 01월 01일 이상이어야 합니다.")
    @Max(value = MAX_BIRTH_YEAR, message = "생년월일은 2030년 12월 31일 이하이어야 합니다.")
    private Integer birth;

}
