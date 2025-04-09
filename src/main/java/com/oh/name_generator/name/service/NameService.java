package com.oh.name_generator.name.service;



import com.oh.name_generator.name.dto.NameRequestDto;
import com.oh.name_generator.name.dto.NameResponseDto;

import java.util.List;

public interface NameService {

    List<NameResponseDto> createName(NameRequestDto nameRequestDto);
}
