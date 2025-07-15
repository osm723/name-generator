package com.oh.name_generator.domain.name.service;


import com.oh.name_generator.domain.name.dto.CreateNameRequestDto;
import com.oh.name_generator.domain.name.dto.NameRequestDto;
import com.oh.name_generator.domain.name.dto.NameResponseDto;
import com.oh.name_generator.domain.name.dto.NameSaveRequestDto;
import com.oh.name_generator.domain.name.repository.NameRepository;
import com.oh.name_generator.external.chatgpt.ChatGptApiClient;
import com.oh.name_generator.global.common.consts.Constants;
import com.oh.name_generator.global.common.utils.CookieUtils;
import com.oh.name_generator.global.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.oh.name_generator.global.common.consts.Constants.Cookie.*;
import static com.oh.name_generator.global.common.consts.Constants.Messages.*;
import static com.oh.name_generator.global.common.consts.Constants.Validation.MAX_NAME_SIZE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NameServiceImpl implements NameService {

    private final NameRepository nameRepository;

    private final ChatGptApiClient chatGptApiClient;

    private final CookieUtils cookieUtils;

    private final Random random = new Random();

    @Override
    public List<NameResponseDto> generateNames(NameRequestDto nameRequestDto) {
        return validateGenerateNames(nameRequestDto);
    }

    @Override
    public List<String> generateNamesWithGpt(NameRequestDto nameRequestDto) {
        return chatGptApiClient.generationNamesWithGpt(nameRequestDto);
    }

    @Override
    public void saveNames(List<NameSaveRequestDto> nameSaveRequestDto, HttpServletRequest request, HttpServletResponse response) {
        if (nameSaveRequestDto == null || nameSaveRequestDto.isEmpty()) {
            throw new BusinessException(NOT_FOUND_REQUEST_NAME);
        }
        nameSaveRequestDto.forEach(saveName -> cookieUtils.setCookie(NAME_COOKIE, saveName, request, response));
    }

    @Override
    public List<NameResponseDto> getSavedNames(HttpServletRequest request) {
        return cookieUtils.getCookie(NAME_COOKIE, request);
    }

    @Override
    public void deleteSavedNames(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.removeCookie(NAME_COOKIE, request, response);
    }

    /**
     * combinationName
     * 성 + 첫번째 이름 + 두번째 이름
     * @param getLastName
     * @param getFirstName
     * @param getSecondName
     * @return String
     */
    private static String combinationName(String getLastName, String getFirstName, String getSecondName) {
        return getLastName + getFirstName + getSecondName;
    }

    /**
     * duplicationCheck
     * 첫번째 이름은 성과 같이 않도록 체크
     * 두번째 이름은 첫번째 이름과 같이 않도록 체크
     * @param createNames
     * @param checkName
     * @return
     */
    private String duplicationCheck(List<CreateNameRequestDto> createNames, String checkName) {
        if (createNames == null || createNames.isEmpty()) {
            throw new BusinessException(NOT_FOUND_NAME_LIST);
        }

        if (createNames.size() == 1) {
            String onlyName = createNames.get(0).getCreateName();
            if (onlyName.equals(checkName)) {
                return onlyName;
            }
        }

        while (true) {
            //Collections.shuffle(createNames);
            String createName = createNames.get(random.nextInt(createNames.size())).getCreateName();

            if (!createName.equals(checkName)) {
                return createName;
            }
        }
    }

    private List<NameResponseDto> validateGenerateNames(NameRequestDto nameRequestDto) {
        int iteratorSize = MAX_NAME_SIZE;
        List<NameResponseDto> createNames = new ArrayList<>();
        List<CreateNameRequestDto> createFirstNames = nameRepository.createFirstNames(nameRequestDto);
        List<CreateNameRequestDto> createSecondNames = nameRepository.createSecondNames(nameRequestDto);

        if (createFirstNames.size() >= MAX_NAME_SIZE && createSecondNames.size() >= MAX_NAME_SIZE) {
            for (int i = 0; i < iteratorSize; i++) {
                String getLastName = nameRequestDto.getLastName() == null ? "" : nameRequestDto.getLastName();
                String getFirstName = nameRequestDto.getFirstName() == null ? "" : nameRequestDto.getFirstName();
                String getSecondName = nameRequestDto.getSecondName() == null ? "" : nameRequestDto.getSecondName();

                // 첫번째 이름은 성과 같이 않도록 체크
                if (getFirstName.isEmpty()) {
                    getFirstName = duplicationCheck(createFirstNames, getLastName);
                }

                // 두번째 이름은 첫번째 이름과 같이 않도록 체크
                if (getSecondName.isEmpty()) {
                    getSecondName = duplicationCheck(createSecondNames, getFirstName);
                }

                // 성 + 첫번째 이름 + 두번째 이름 조합
                String createName = combinationName(getLastName, getFirstName, getSecondName);

                // 기존 생성된 이름과 중복 체크
                boolean duplicationName = createNames.stream().anyMatch(m -> Objects.equals(m.getCreateName(), createName));
                log.info("nameGenerations={}, duplicationName={}", createName, duplicationName);

                if (duplicationName) {
                    iteratorSize++;
                } else {
                    createNames.add(new NameResponseDto(createName));
                }
            }
        } else {
            throw new BusinessException(NAME_GENERATION_FAIL);
        }
        return createNames;
    }


}
