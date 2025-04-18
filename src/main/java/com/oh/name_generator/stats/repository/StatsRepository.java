package com.oh.name_generator.stats.repository;

import com.oh.name_generator.stats.entity.NameStats;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatsRepository extends JpaRepository<NameStats, Long>, StatsRepositoryQuery {

    List<NameStats> findAll();

    NameStats findByNameAndYears(@NotEmpty String name, @NotEmpty @Size(max = 4, min = 4) Integer years);

    //List<NameStats> findByWhere(NameRequestCond nameRequestCond);


}
