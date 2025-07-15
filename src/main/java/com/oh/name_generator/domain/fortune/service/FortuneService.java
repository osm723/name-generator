package com.oh.name_generator.domain.fortune.service;


import com.oh.name_generator.domain.fortune.dto.FortuneRequestDto;
import com.oh.name_generator.domain.fortune.dto.FortuneResponseDto;
import com.oh.name_generator.domain.fortune.dto.FortuneSaveRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface FortuneService {

    FortuneResponseDto generateFortune(@Valid FortuneRequestDto fortuneRequestDto);

    void saveFortune(@Valid FortuneSaveRequestDto fortuneSaveRequestDto, HttpServletRequest request, HttpServletResponse response);

    List<FortuneResponseDto> getSavedFortunes(HttpServletRequest request);

    void deleteSavedFortunes(HttpServletRequest request, HttpServletResponse response);
}
