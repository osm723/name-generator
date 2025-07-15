package com.oh.name_generator.domain.name.service;

import com.oh.name_generator.domain.name.dto.NameRequestDto;
import com.oh.name_generator.domain.name.dto.NameResponseDto;
import com.oh.name_generator.domain.name.dto.NameSaveRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface NameService {

    List<NameResponseDto> generateNames(@Valid NameRequestDto nameRequestDto);

    List<String> generateNamesWithGpt(@Valid NameRequestDto nameRequestDto);

    void saveNames(@Valid List<NameSaveRequestDto> nameSaveRequestDto, HttpServletRequest request, HttpServletResponse response);

    List<NameResponseDto> getSavedNames(HttpServletRequest request);

    void deleteSavedNames(HttpServletRequest request, HttpServletResponse response);
}
