package com.oh.name_generator.domain.fortune.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class FortuneResponseDto {

    private String inputWord;

    private Integer birth;

    private String fortuneTelling;

    private LocalDate fortuneDate;

    private String zodiacSigns;

    @JsonIgnore
    private final String[] zodiacs = {
            "쥐", "소", "호랑이", "토끼", "용", "뱀",
            "말", "양", "원숭이", "닭", "개", "돼지"
    };

    public FortuneResponseDto(String inputWord, Integer birth, String fortuneTelling, LocalDate fortuneDate) {
        this.inputWord = inputWord;
        this.birth = birth;
        this.fortuneTelling = fortuneTelling;
        this.fortuneDate = fortuneDate;
        this.zodiacSigns = calZodiacSigns(birth);
    }

    private String calZodiacSigns(int birthDate) {
        LocalDate birthLocalDate = LocalDate.parse(String.valueOf(birthDate), DateTimeFormatter.ofPattern("yyyyMMdd"));
        int zodiacIndex = (birthLocalDate.getYear() - 4) % 12;
        return zodiacs[zodiacIndex];
    }
}
