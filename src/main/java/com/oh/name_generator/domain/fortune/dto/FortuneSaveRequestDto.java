package com.oh.name_generator.domain.fortune.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FortuneSaveRequestDto {

    private String inputWord;

    private Integer birth;

    private String fortuneTelling;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fortuneDate;

    private String zodiacSigns;
}
