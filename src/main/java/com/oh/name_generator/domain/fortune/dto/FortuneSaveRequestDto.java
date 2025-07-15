package com.oh.name_generator.domain.fortune.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FortuneSaveRequestDto {

    private String inputWord;

    private Integer birth;

    private String fortuneTelling;

    private List<Integer> fortuneDate;

    private String zodiacSigns;
}
