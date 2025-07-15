package com.oh.name_generator.domain.stats.repository;

import com.oh.name_generator.domain.stats.dto.StatsRequestCond;
import com.oh.name_generator.domain.stats.entity.NameStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StatsRepositoryQuery {

    Page<NameStats> findStatsNamesByCond(Pageable pageable, StatsRequestCond statsRequestCond);

    Map<Integer, Long> findStatsYearCountByName(StatsRequestCond statsRequestCond);

    Map<Integer, Integer> findStatsYearRankByName(StatsRequestCond statsRequestCond);

}
