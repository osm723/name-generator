package com.oh.name_generator.stats.dto;

import com.oh.name_generator.stats.entity.NameStats;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.NumberFormat;

@Getter
public class StatsResponseDto {

    public StatsResponseDto(NameStats nameStats) {
        this.name = nameStats.getName();
        this.gender = nameStats.getGender();
        this.years = nameStats.getYears();
        this.yearRank = nameStats.getYearRank();
        this.yearCount = nameStats.getYearCount();
    }

    @QueryProjection
    public StatsResponseDto(String name, String gender, Integer years, Integer yearRank, Long yearCount, Long totalCount, Double totalAvgRank, Integer totalMaxRank, Integer totalMinRank, Long totalRankCount) {
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
    @Size(max = 1)
    private String gender;

    @NotEmpty
    @Min(2008)
    @Max(2040)
    private Integer years;

    @NotEmpty
    @Min(1)
    @Max(20)
    private Integer yearRank;

    @NotEmpty
    private Long yearCount;

    private Long totalCount;      // sum 결과는 Long

    @NumberFormat(pattern = "#.#") // 소수점 첫번째 자리까지
    private Double totalAvgRank;  // avg 결과는 Double

    private Integer totalMaxRank; // min, max 결과는 Integer

    private Integer totalMinRank;

    private Long totalRankCount;



}
