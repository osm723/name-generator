package com.oh.name_generator.external.api.controller;

import com.oh.name_generator.external.chatgpt.ChatGptApiClient;
import com.oh.name_generator.global.common.utils.CookieUtils;
import com.oh.name_generator.domain.name.dto.NameRequestDto;
import com.oh.name_generator.domain.name.dto.NameSaveRequestDto;
import com.oh.name_generator.domain.name.service.NameService;
import com.oh.name_generator.domain.stats.dto.StatsRequestDto;
import com.oh.name_generator.domain.stats.dto.StatsResponseDto;
import com.oh.name_generator.domain.stats.service.StatsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final StatsService statsService;

    private final CookieUtils cookieUtils;

    private final NameService nameService;

    private final ChatGptApiClient chatGptApiClient;

    @Value("${cookie.name_cookie}")
    private String cookieName;

    /**
     * statsNamesApi
     * 이름 통계 조회 Api
     * @param pageable
     * @param statsRequestDto
     * @return names
     */
    @PostMapping("/stats/names")
    public Page<StatsResponseDto> statsNamesApi(Pageable pageable, @Validated StatsRequestDto statsRequestDto) {
        return statsService.findByWhere(pageable, statsRequestDto);
    }

    /**
     * saveNamesApi
     * 이름 저장 조회 Api
     * 개인 쿠키값을 가져오는 구조라 사실 API는 무의미 하지만 만듬
     * @param request
     * @return names
     */
    @PostMapping("/saved/names")
    public Map<String, Object> saveNamesApi(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", cookieUtils.getCookie(cookieName, request));
        return response;
    }

    /**
     * saveNamesApi
     * 이름 저장 Api
     * @param request
     * @return names
     */
    @PostMapping("/save/name")
    public ResponseEntity<Void> saveName(NameSaveRequestDto saveName, HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.setCookie(cookieName ,saveName, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * generationNames
     * 이름 생성 조회 Api
     * @param nameRequestDto
     * @return names
     */
    //@RequestMapping("/generation/names")
    @PostMapping("/generation/names")
    public Map<String, Object> generationNames(@Validated NameRequestDto nameRequestDto) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", nameService.generateNames(nameRequestDto));
        return response;
    }

    /**
     * generationNamesWithGpt
     * 이름 생성 조회 Api (gpt)
     * @param nameRequestDto
     * @return names
     */
    //@RequestMapping("/generation/names/gpt")
    @PostMapping("/generation/names/gpt")
    public Map<String, Object> generationNamesWithGpt(@Validated NameRequestDto nameRequestDto) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", chatGptApiClient.generationNamesWithGpt(nameRequestDto));
        return response;
    }

}
