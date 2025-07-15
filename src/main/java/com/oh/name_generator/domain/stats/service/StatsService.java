package com.oh.name_generator.domain.stats.service;

import com.oh.name_generator.domain.stats.dto.StatsPopupRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsPopupResponseDto;
import com.oh.name_generator.domain.stats.dto.StatsRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StatsService {

    Page<StatsResponseDto> getStatsNames(Pageable pageable, StatsRequestDto statsRequestDto);

    StatsPopupResponseDto getStatsNameByNameAndYears(StatsPopupRequestDto statsPopupRequestDto);

    Map<Integer, Long> getStatsYearCountChart(StatsPopupRequestDto statsPopupRequestDto);

    Map<Integer, Integer> getStatsYearRankChart(StatsPopupRequestDto statsPopupRequestDto);


}
