package com.oh.name_generator.stats.entity;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Getter
@Table(name = "CF_NAME_STATS", schema = "GEN")
public class NameStats {

    public NameStats() {
    }

    public NameStats(Integer years, Integer yearRank, Long yearCount) {
        this.years = years;
        this.yearRank = yearRank;
        this.yearCount = yearCount;
    }

    @QueryProjection
    public NameStats(String name, String gender, Integer years, Integer yearRank, Long yearCount, Long totalCount, Double totalAvgRank, Integer totalMaxRank, Integer totalMinRank, Long totalRankCount) {
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotEmpty
    private String name;

    @NotEmpty
    @Size(max = 2, min = 2)
    private String gender;

    @NotEmpty
    @Size(max = 4, min = 4)
    private Integer years;

    @NotEmpty
    @Size(max = 2)
    private Integer yearRank;

    @NotEmpty
    private Long yearCount;

    private Integer totalRank;

    private Long totalCount;      // sum 결과는 Long

    @NumberFormat(pattern = "#.#") // 소수점 첫번째 자리까지
    private Double totalAvgRank;  // avg 결과는 Double

    private Integer totalMaxRank; // min, max 결과는 Integer

    private Integer totalMinRank;

    private Long totalRankCount;
}
