package com.oh.name_generator.domain.stats.repository;

import com.oh.name_generator.domain.stats.dto.StatsRequestCond;
import com.oh.name_generator.domain.stats.dto.StatsResponseDto;
import com.oh.name_generator.domain.stats.entity.NameStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StatsRepositoryQuery {

    Page<NameStats> findByWhere(Pageable pageable, StatsRequestCond statsRequestCond);

    //Page<NameStats> findByWhere(Pageable pageable, NameStats nameStats);

    Page<StatsResponseDto> findByCond(Pageable pageable, StatsRequestCond statsRequestCond);

    Map<Integer, Long> findYearCountByName(StatsRequestCond statsRequestCond);

    Map<Integer, Integer> findYearRankByName(StatsRequestCond statsRequestCond);

}
