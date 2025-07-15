package com.oh.name_generator.domain.name.repository;



import com.oh.name_generator.domain.name.dto.CreateNameRequestDto;
import com.oh.name_generator.domain.name.dto.NameRequestDto;

import java.util.List;

public interface NameRepository {

    List<CreateNameRequestDto> createFirstNames(NameRequestDto nameRequestDto);

    List<CreateNameRequestDto> createSecondNames(NameRequestDto nameRequestDto);
}
