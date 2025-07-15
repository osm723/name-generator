package com.oh.name_generator.domain.name.controller;

import com.oh.name_generator.global.common.consts.Constants;
import com.oh.name_generator.global.common.dto.ApiResponse;
import com.oh.name_generator.domain.name.dto.NameRequestDto;
import com.oh.name_generator.domain.name.dto.NameResponseDto;
import com.oh.name_generator.domain.name.dto.NameSaveRequestDto;
import com.oh.name_generator.domain.name.service.NameService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.oh.name_generator.global.common.consts.Constants.Api.*;
import static com.oh.name_generator.global.common.consts.Constants.Messages.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_PREFIX + "/names")
public class NameController {

    private final NameService nameService;

    /**
     * 이름 생성
     * generateNames
     * @param nameRequestDto
     * @return ApiResponse<List<NameResponseDto>>
     */
    @PostMapping("/generate")
    public ApiResponse<List<NameResponseDto>> generateNames(@Valid @RequestBody NameRequestDto nameRequestDto) {
        List<NameResponseDto> names = nameService.generateNames(nameRequestDto);
        return ApiResponse.success(names, NAME_GENERATION_SUCCESS);
    }

    /**
     * 이름 생성 (GPT)
     * generateNamesWithGpt
     * @param nameRequestDto
     * @return ApiResponse<List<NameResponseDto>>
     */
    @PostMapping("/generate/gpt")
    public ApiResponse<List<String>> generateNamesWithGpt(@Valid @RequestBody NameRequestDto nameRequestDto) {
        List<String> names = nameService.generateNamesWithGpt(nameRequestDto);
        return ApiResponse.success(names, NAME_GENERATION_SUCCESS);
    }

    /**
     * 이름 저장 (쿠키)
     * saveNames
     * @param nameSaveRequestDto
     * @param request
     * @param response
     * @return ApiResponse<Void>
     */
    @PostMapping("/save")
    public ApiResponse<Void> saveName(@Valid @RequestBody NameSaveRequestDto nameSaveRequestDto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        nameService.saveName(nameSaveRequestDto, request, response);
        return ApiResponse.success(null, Constants.Messages.SAVE_SUCCESS);
    }

    /**
     * 이름 조회 (쿠키)
     * getSavedNames
     * @param request
     * @return ApiResponse<List<NameResponseDto>>
     */
    @GetMapping("/saved")
    public ApiResponse<List<NameResponseDto>> getSavedNames(HttpServletRequest request) {
        List<NameResponseDto> savedNames = nameService.getSavedNames(request);
        return ApiResponse.success(savedNames);
    }

    /**
     * 이름 삭제 (쿠키)
     * deleteSavedNames
     * @param request
     * @param response
     * @return ApiResponse<Void>
     */
    @DeleteMapping("/saved")
    public ApiResponse<Void> deleteSavedNames(HttpServletRequest request, HttpServletResponse response) {
        nameService.deleteSavedNames(request, response);
        return ApiResponse.success(null, Constants.Messages.DELETE_SUCCESS);
    }


}
