package com.oh.name_generator.domain.stats.controller;

import com.oh.name_generator.domain.stats.dto.StatsPopupRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsPopupResponseDto;
import com.oh.name_generator.domain.stats.dto.StatsRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsResponseDto;
import com.oh.name_generator.domain.stats.service.StatsService;
import com.oh.name_generator.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.oh.name_generator.global.common.consts.Constants.Api.API_V1_PREFIX;
import static com.oh.name_generator.global.common.consts.Constants.Messages.STATS_SEARCH_SUCCESS;


@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_PREFIX + "/stats")
public class StatsController {

    private final StatsService statsService;

    /**
     * 이름 통계 조건부 조회 (화면 페이징)
     * getStatsNames
     * @param statsRequestDto
     * @param pageable
     * @return ApiResponse<Map<String, Object>>
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> getStatsNames(@ModelAttribute("statsName") @Validated StatsRequestDto statsRequestDto, Pageable pageable) {
        Page<StatsResponseDto> names = statsService.getStatsNames(pageable, statsRequestDto);
        return ApiResponse.success(setResponseData(pageable, names), STATS_SEARCH_SUCCESS);
    }

    /**
     * 이름 통계 상세 페이지
     * getStatsName
     * @param name
     * @param years
     * @return ApiResponse<StatsPopupResponseDto>
     */
    @GetMapping("/detail")
    public ApiResponse<StatsPopupResponseDto> getStatsName(@RequestParam String name, @RequestParam int years) {
        StatsPopupResponseDto popupName = statsService.getStatsNameByNameAndYears(new StatsPopupRequestDto(name, years));
        return ApiResponse.success(popupName, STATS_SEARCH_SUCCESS);
    }

    /**
     * 연간 등록건수 차트 데이터 가져오기
     * getStatsYearCountChart
     * @param statsPopupRequestDto
     * @return ApiResponse<Map<Integer, Long>>
     */
    @GetMapping("/chart/yearCount")
    public ApiResponse<Map<Integer, Long>> getStatsYearCountChart(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto) {
        Map<Integer, Long> chartNames = statsService.getStatsYearCountChart(statsPopupRequestDto);
        return ApiResponse.success(chartNames, STATS_SEARCH_SUCCESS);
    }

    /**
     * 연간 순위 차트 데이터 가져오기
     * getStatsYearRankChart
     * @param statsPopupRequestDto
     * @return ApiResponse<Map<Integer, Integer>>
     */
    @GetMapping("/chart/yearRank")
    public ApiResponse<Map<Integer, Integer>> getStatsYearRankChart(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto) {
        Map<Integer, Integer> chartNames = statsService.getStatsYearRankChart(statsPopupRequestDto);
        return ApiResponse.success(chartNames, STATS_SEARCH_SUCCESS);
    }

    private static Map<String, Object> setResponseData(Pageable pageable, Page<StatsResponseDto> names) {
        Map<String, Object> response = new HashMap<>();
        response.put("names", names);
        response.put("isAscending", pageable.getSort().stream()
                .findFirst()
                .map(Sort.Order::isAscending)
                .orElse(true));
        response.put("sortField", pageable.getSort().stream()
                .findFirst()
                .map(Sort.Order::getProperty)
                .orElse("years"));
        return response;
    }

}

