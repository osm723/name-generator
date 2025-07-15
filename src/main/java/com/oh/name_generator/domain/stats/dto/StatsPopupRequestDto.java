package com.oh.name_generator.domain.stats.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatsPopupRequestDto {

    //@NotEmpty
    @Size(max = 2)
    private String name;

    @Min(2008)
    @Max(2040)
    private Integer years;
}
