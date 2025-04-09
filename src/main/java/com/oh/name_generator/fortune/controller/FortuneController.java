package com.oh.name_generator.fortune.controller;

import com.oh.name_generator.common.exception.BizException;
import com.oh.name_generator.common.utils.CookieUtils;
import com.oh.name_generator.fortune.dto.FortuneRequestDto;
import com.oh.name_generator.fortune.dto.FortuneResponseDto;
import com.oh.name_generator.fortune.dto.FortuneSaveRequestDto;
import com.oh.name_generator.fortune.service.FortuneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FortuneController {

    private final FortuneService fortuneService;

    private final CookieUtils cookieUtils;

    private static final String COOKIE_NAME = "FORTUNE";

    /**
     * 운세 생성 (입력된 1개의 단어와 생년월일)
     * createFortune
     * @param fortuneRequestDto
     * @return Map<String, Object>
     */
    @PostMapping("/fortune/create")
    public Map<String, Object> createFortune(@RequestBody @Valid FortuneRequestDto fortuneRequestDto) {
        if (String.valueOf(fortuneRequestDto.getBirth()).length() != 8) {
            throw new BizException("BizException : 입력된 생년월일이 8자리가 아닙니다.");
        }

        Map<String, Object> response = new HashMap<>();
        FortuneResponseDto fortuneTelling = fortuneService.generationFortune(fortuneRequestDto);
        response.put("content", fortuneTelling);
        return response;
    }

    /**
     * 운세 조회
     * getFortune
     * @return Map<String, Object>
     */
    @GetMapping("/fortune")
    public Map<String, Object> getFortune(HttpServletRequest request) {
        List<FortuneResponseDto> fortune = cookieUtils.getCookie(COOKIE_NAME, request);

        Map<String, Object> response = new HashMap<>();
        response.put("content", fortune);
        response.put("count", fortune.size());
        return response;
    }

    /**
     * 운세 저장
     * saveFortune
     * @return ResponseEntity<Void>
     */
    @PostMapping("/fortune")
    public ResponseEntity<Void> saveFortune(@RequestBody FortuneSaveRequestDto fortune, HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.setCookie(COOKIE_NAME, fortune, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 운세 삭제
     * deleteFortune
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/fortune")
    public ResponseEntity<Void> deleteFortune(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.removeCookie(COOKIE_NAME, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
