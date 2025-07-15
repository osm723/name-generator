package com.oh.name_generator.domain.stats.repository;

import com.oh.name_generator.domain.stats.entity.NameStats;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<NameStats, Long>, StatsRepositoryQuery {

    NameStats findStatsByNameAndYears(@NotEmpty String name, @NotNull @Size(max = 4, min = 4) Integer years);


}
