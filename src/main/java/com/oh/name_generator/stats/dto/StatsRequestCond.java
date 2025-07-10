package com.oh.name_generator.stats.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsRequestCond {

    private String name;

    @Size(max = 2, min = 2)
    private String gender;

    @Min(2008)
    @Max(2030)
    private Integer startYear;

    @Min(2008)
    @Max(2030)
    private Integer endYear;

    @Min(1)
    @Max(20)
    private Integer yearRank;

    @Min(1)
    @Max(20)
    private Integer totalRank;

    private Integer yearCount;

    private String sortField;

    private Boolean isAscending;
}
