package com.oh.name_generator.domain.stats.controller;

import com.oh.name_generator.domain.stats.dto.StatsPopupRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsPopupResponseDto;
import com.oh.name_generator.domain.stats.dto.StatsRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsResponseDto;
import com.oh.name_generator.domain.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.oh.name_generator.global.common.consts.Constants.Api.API_V1_PREFIX;


@Controller
@RequiredArgsConstructor
@RequestMapping(API_V1_PREFIX + "/stats")
public class StatsController {

    private final StatsService statsService;

    /**
     * statsNames
     * 이름 통계 조건부 조회
     * @param statsRequestDto
     * @param model
     * @param pageable
     * @return viewPath
     */
//    @GetMapping("/statsNames")
//    public String statsNames(@ModelAttribute("statsName") @Validated StatsRequestDto statsRequestDto, Model model, Pageable pageable) {
//        Page<StatsResponseDto> names = statsService.findByWhere(pageable, statsRequestDto);
//        model.addAttribute("names", names);
//        return "name/stats/statsMain";
//    }

    /**
     * statsNamesPage
     * 이름 통계 조건부 조회 (화면 페이징)
     * @param statsRequestDto
     * @param pageable
     * @return status
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatsNames(@ModelAttribute("statsName") @Validated StatsRequestDto statsRequestDto, Pageable pageable) {
        Page<StatsResponseDto> names = statsService.getStatsNames(pageable, statsRequestDto);

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

        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<StatsPopupResponseDto> getStatsName(@RequestParam String name, @RequestParam int years) {
        StatsPopupResponseDto popupName = statsService.getStatsNameByNameAndYears(new StatsPopupRequestDto(name, years));
        return ResponseEntity.ok(popupName);
    }

    /**
     * statsYearCountChart
     * 연간 등록건수 차트 데이터 가져오기
     * @param statsPopupRequestDto
     * @return response
     */
    @GetMapping("/chart/yearCount")
    @ResponseBody
    public Map<String, Object> getStatsYearCountChart(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto) {
        Map<String, Object> response = new HashMap<>();
        Map<Integer, Long> chartNames = statsService.getStatsYearCountChart(statsPopupRequestDto);
        response.put("chartNames", chartNames);
        return response;
    }

    /**
     * statsYearRankChart
     * 연간 순위 차트 데이터 가져오기
     * @param statsPopupRequestDto
     * @return response
     */
    @GetMapping("/chart/yearRank")
    @ResponseBody
    public Map<String, Object> getStatsYearRankChart(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto) {
        Map<String, Object> response = new HashMap<>();
        Map<Integer, Integer> chartNames = statsService.getStatsYearRankChart(statsPopupRequestDto);
        response.put("chartNames", chartNames);
        return response;
    }

}

