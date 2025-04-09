package com.oh.name_generator.fortune.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FortuneSaveRequestDto {

    private String inputWord;

    private Integer birth;

    private String fortuneTelling;

    private String fortuneDate;

    private String zodiacSigns;
}
