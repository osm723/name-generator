package com.oh.name_generator.domain.stats.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Getter
@Setter
public class StatsPopupResponseDto {

    public StatsPopupResponseDto() {
    }

    public StatsPopupResponseDto(String name, String gender, Integer years, Integer yearRank, Long yearCount) {
        this.name = name;
        this.gender = gender;
        this.years = years;
        this.yearRank = yearRank;
        this.yearCount = yearCount;
    }

    public StatsPopupResponseDto(String name, String gender, Integer years, Integer yearRank, Long yearCount, Long totalCount, Double totalAvgRank, Integer totalMaxRank, Integer totalMinRank, Long totalRankCount) {
        this.name = name;
        this.gender = gender;
        this.years = years;
        this.yearRank = yearRank;
        this.yearCount = yearCount;
        this.totalCount = totalCount;
        this.totalAvgRank = totalAvgRank;
        this.totalMaxRank = totalMaxRank;
        this.totalMinRank = totalMinRank;
        this.totalRankCount = totalRankCount;
    }

    @NotEmpty
    private String name;

    @NotEmpty
    @Size(max = 2, min = 2)
    private String gender;

    @NotNull
    @Min(2008)
    @Max(2030)
    private Integer years;

    @NotNull
    @Min(1)
    @Max(20)
    private Integer yearRank;

    @NotNull
    private Long yearCount;

    private List<String> firstNameTranslate;

    private List<String> secondNameTranslate;

    private Long totalCount;      // sum 결과는 Long

    @NumberFormat(pattern = "#.#") // 소수점 첫번째 자리까지
    private Double totalAvgRank;  // avg 결과는 Double

    @Min(1)
    @Max(20)
    private Integer totalMaxRank; // min, max 결과는 Integer

    @Min(1)
    @Max(20)
    private Integer totalMinRank;

    private Long totalRankCount;

}
