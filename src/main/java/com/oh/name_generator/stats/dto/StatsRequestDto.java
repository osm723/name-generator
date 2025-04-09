package com.oh.name_generator.stats.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsRequestDto {

    @Size(max = 2)
    private String name;

    @Size(max = 1)
    private String gender;

    @Min(2008)
    @Max(2040)
    private Integer startYear;

    @Min(2008)
    @Max(2040)
    private Integer endYear;

    @Min(1)
    @Max(20)
    private Integer yearRank;

    private Integer totalRank;

    @Min(1)
    @Max(10000)
    private Integer yearCount;

    private String sortField = "years";

    private Boolean isAscending = true;
}
