package com.oh.name_generator.domain.fortune.controller;

import com.oh.name_generator.global.common.consts.Constants;
import com.oh.name_generator.global.common.dto.ApiResponse;
import com.oh.name_generator.domain.fortune.dto.FortuneRequestDto;
import com.oh.name_generator.domain.fortune.dto.FortuneResponseDto;
import com.oh.name_generator.domain.fortune.dto.FortuneSaveRequestDto;
import com.oh.name_generator.domain.fortune.service.FortuneService;
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

import static com.oh.name_generator.global.common.consts.Constants.Api.API_V1_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_PREFIX + "/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    /**
     * 운세 생성 (입력된 1개의 단어와 생년월일)
     * generateFortune
     * @param fortuneRequestDto
     * @return Map<String, Object>
     */
    @PostMapping("/generate")
    public ApiResponse<FortuneResponseDto> generateFortune(@Valid @RequestBody FortuneRequestDto fortuneRequestDto) {
        FortuneResponseDto fortune = fortuneService.generateFortune(fortuneRequestDto);
        return ApiResponse.success(fortune, Constants.Messages.FORTUNE_GENERATION_SUCCESS);
    }

    /**
     * 운세 저장 (쿠키)
     * saveFortune
     * @param fortuneSaveRequestDto
     * @param request
     * @param response
     * @return ApiResponse<Void>
     */
    @PostMapping("/save")
    public ApiResponse<Void> saveFortune(@Valid @RequestBody FortuneSaveRequestDto fortuneSaveRequestDto,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        fortuneService.saveFortune(fortuneSaveRequestDto, request, response);
        return ApiResponse.success(null, Constants.Messages.SAVE_SUCCESS);
    }

    /**
     * 운세 조회 (쿠키)
     * getSavedFortunes
     * @param request
     * @return ApiResponse<List<FortuneResponseDto>>
     */
    @GetMapping("/saved")
    public ApiResponse<List<FortuneResponseDto>> getSavedFortunes(HttpServletRequest request) {
        List<FortuneResponseDto> savedFortunes = fortuneService.getSavedFortunes(request);
        return ApiResponse.success(savedFortunes);
    }

    @DeleteMapping("/saved")
    public ApiResponse<Void> deleteSavedFortunes(HttpServletRequest request, HttpServletResponse response) {
        fortuneService.deleteSavedFortunes(request, response);
        return ApiResponse.success(null, Constants.Messages.DELETE_SUCCESS);
    }

}
