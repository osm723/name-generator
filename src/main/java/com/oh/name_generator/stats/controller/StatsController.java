package com.oh.name_generator.stats.controller;

import com.oh.name_generator.stats.dto.StatsPopupRequestDto;
import com.oh.name_generator.stats.dto.StatsPopupResponseDto;
import com.oh.name_generator.stats.dto.StatsRequestDto;
import com.oh.name_generator.stats.dto.StatsResponseDto;
import com.oh.name_generator.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
public class StatsController {

    private final StatsService statsService;

    /**
     * namesAll
     * 이름 통계 전체 조회
     * @param statsRequestDto
     * @param model
     * @param pageable
     * @return viewPath
     */
    @GetMapping("/statsNamesAll")
    public String statsNamesAll(@ModelAttribute("statsName") @Validated StatsRequestDto statsRequestDto, Model model, Pageable pageable) {
        Page<StatsResponseDto> names = statsService.findAll(pageable);
        model.addAttribute("names", names);
        return "name/stats/statsMain";
    }

    /**
     * statsMain
     * 이름 통계 초기화면
     * @param statsRequestDto
     * @return viewPath
     */
    @GetMapping("/main")
    public String statsMain(@ModelAttribute("statsName") StatsRequestDto statsRequestDto) {
        return "name/stats/statsMain";
    }

    /**
     * statsNames
     * 이름 통계 조건부 조회
     * @param statsRequestDto
     * @param model
     * @param pageable
     * @return viewPath
     */
    @GetMapping("/statsNames")
    public String statsNames(@ModelAttribute("statsName") @Validated StatsRequestDto statsRequestDto, Model model, Pageable pageable) {
        Page<StatsResponseDto> names = statsService.findByWhere(pageable, statsRequestDto);
        model.addAttribute("names", names);
        return "name/stats/statsMain";
    }

    /**
     * statsNamesPage
     * 이름 통계 조건부 조회 (화면 페이징)
     * @param statsRequestDto
     * @param pageable
     * @return status
     */
    @GetMapping("/statsNamesPage")
    public ResponseEntity<Map<String, Object>> statsNamesPage(@ModelAttribute("statsName") @Validated StatsRequestDto statsRequestDto, Pageable pageable) {
        Page<StatsResponseDto> names = statsService.findByWhere(pageable, statsRequestDto);

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

    /**
     * statsPopup
     * 이름 상세 페이지
     * @param statsPopupRequestDto
     * @param model
     * @return viewPath
     */
    @GetMapping("/statsPopup")
    public String statsPopup(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto, Model model) {
        StatsPopupResponseDto popupName = statsService.findPopupByNameAndYears(statsPopupRequestDto);
        model.addAttribute("popupName", popupName);
        return "name/stats/popup";
    }

    /**
     * statsYearCountChart
     * 연간 등록건수 차트 데이터 가져오기
     * @param statsPopupRequestDto
     * @return response
     */
    @GetMapping("/statsChart/yearCount")
    @ResponseBody
    public Map<String, Object> statsYearCountChart(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto) {
        Map<String, Object> response = new HashMap<>();
        Map<Integer, Long> chartNames = statsService.findYearCountByName(statsPopupRequestDto);
        response.put("chartNames", chartNames);
        return response;
    }

    /**
     * statsYearRankChart
     * 연간 순위 차트 데이터 가져오기
     * @param statsPopupRequestDto
     * @return response
     */
    @GetMapping("/statsChart/yearRank")
    @ResponseBody
    public Map<String, Object> statsYearRankChart(@ModelAttribute @Validated StatsPopupRequestDto statsPopupRequestDto) {
        Map<String, Object> response = new HashMap<>();
        Map<Integer, Integer> chartNames = statsService.findYearRankByName(statsPopupRequestDto);
        response.put("chartNames", chartNames);
        return response;
    }

}

