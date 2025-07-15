package com.oh.name_generator.domain.stats.repository;

import com.oh.name_generator.domain.stats.entity.NameStats;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatsRepository extends JpaRepository<NameStats, Long>, StatsRepositoryQuery {

    List<NameStats> findAll();

    NameStats findByNameAndYears(@NotEmpty String name, @NotNull @Size(max = 4, min = 4) Integer years);

    //List<NameStats> findByWhere(NameRequestCond nameRequestCond);


}
