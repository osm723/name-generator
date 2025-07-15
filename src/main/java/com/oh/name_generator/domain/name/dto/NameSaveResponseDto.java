package com.oh.name_generator.domain.name.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameSaveResponseDto {

    @NotBlank(message = "저장할 이름은 필수입니다.")
    @Size(max = 10, message = "이름은 최대 10자까지 가능합니다.")
    private String saveName;
}
