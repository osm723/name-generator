package com.oh.name_generator.name.service;


import com.oh.name_generator.common.exception.BizException;
import com.oh.name_generator.name.dto.CreateNameRequestDto;
import com.oh.name_generator.name.dto.NameRequestDto;
import com.oh.name_generator.name.dto.NameResponseDto;
import com.oh.name_generator.name.repository.NameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NameServiceImpl implements NameService {

    private final NameRepository nameRepository;

    @Value("${max.name_size}")
    private int maxNameSize;

    private final Random random = new Random();

    @Override
    public List<NameResponseDto> createName(NameRequestDto nameRequestDto) {
        int iteratorSize = maxNameSize;
        List<NameResponseDto> createNames = new ArrayList<>();
        List<CreateNameRequestDto> createFirstNames = nameRepository.createFirstNames(nameRequestDto);
        List<CreateNameRequestDto> createSecondNames = nameRepository.createSecondNames(nameRequestDto);

        if (createFirstNames.size() >= maxNameSize && createSecondNames.size() >= maxNameSize) {
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
            throw new BizException("이름 생성중 문제가 발생했습니다.");
        }
        return createNames;
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
            throw new BizException("이름 목록이 존재하지 않습니다.");
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


}
