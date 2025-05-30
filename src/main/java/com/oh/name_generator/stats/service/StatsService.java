package com.oh.name_generator.stats.service;

import com.oh.name_generator.stats.dto.StatsPopupRequestDto;
import com.oh.name_generator.stats.dto.StatsPopupResponseDto;
import com.oh.name_generator.stats.dto.StatsRequestDto;
import com.oh.name_generator.stats.dto.StatsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StatsService {

    Page<StatsResponseDto> findAll(Pageable pageable);

    Page<StatsResponseDto> findByWhere(Pageable pageable, StatsRequestDto statsRequestDto);

    StatsPopupResponseDto findPopupByNameAndYears(StatsPopupRequestDto statsPopupRequestDto);

    Map<Integer, Long> findYearCountByName(StatsPopupRequestDto statsPopupRequestDto);

    Map<Integer, Integer> findYearRankByName(StatsPopupRequestDto statsPopupRequestDto);


}
