package com.oh.name_generator.fortune.service;


import com.oh.name_generator.fortune.dto.FortuneRequestDto;
import com.oh.name_generator.fortune.dto.FortuneResponseDto;

public interface FortuneService {

    FortuneResponseDto generationFortune(FortuneRequestDto fortuneRequestDto);
}
