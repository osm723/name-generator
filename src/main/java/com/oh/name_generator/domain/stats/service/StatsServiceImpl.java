package com.oh.name_generator.domain.stats.service;

import com.oh.name_generator.domain.stats.dto.*;
import com.oh.name_generator.domain.stats.entity.NameStats;
import com.oh.name_generator.domain.stats.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.oh.name_generator.global.common.consts.Constants.FileDir.TRANSLATE_FILE_NAME;
import static java.nio.charset.StandardCharsets.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    private final ModelMapper modelMapper;

    @Override
    public Page<StatsResponseDto> getStatsNames(Pageable pageable, StatsRequestDto statsRequestDto) {
        Page<NameStats> names = statsRepository.findStatsNamesByCond(pageable, modelMapper.map(statsRequestDto, StatsRequestCond.class));
        return transformStatsResponseDto(names);
    }

    @Override
    public StatsPopupResponseDto getStatsNameByNameAndYears(StatsPopupRequestDto statsPopupRequestDto) {
        NameStats name = statsRepository.findStatsByNameAndYears(statsPopupRequestDto.getName(), statsPopupRequestDto.getYears());
        StatsPopupResponseDto statsPopupResponseDto = modelMapper.map(name, StatsPopupResponseDto.class);

        // 한자 찾기
        String translateName = statsPopupRequestDto.getName();
        if (translateName.length() == 2) {
            statsPopupResponseDto.setFirstNameTranslate(translate(translateName.substring(0, 1)));
            statsPopupResponseDto.setSecondNameTranslate(translate(translateName.substring(1, 2)));
        }
        return statsPopupResponseDto;
    }

    @Override
    public Map<Integer, Long> getStatsYearCountChart(StatsPopupRequestDto statsPopupRequestDto) {
        return statsRepository.findStatsYearCountByName(modelMapper.map(statsPopupRequestDto, StatsRequestCond.class));
    }

    @Override
    public Map<Integer, Integer> getStatsYearRankChart(StatsPopupRequestDto statsPopupRequestDto) {
        return statsRepository.findStatsYearRankByName(modelMapper.map(statsPopupRequestDto, StatsRequestCond.class));
    }

    private Page<StatsResponseDto> transformStatsResponseDto(Page<NameStats> names) {
        return names.map(nameStats -> new StatsResponseDto(
                nameStats.getName(),
                nameStats.getGender(),
                nameStats.getYears(),
                nameStats.getYearRank(),
                nameStats.getYearCount(),
                nameStats.getTotalCount(),
                nameStats.getTotalAvgRank(),
                nameStats.getTotalMaxRank(),
                nameStats.getTotalMinRank(),
                nameStats.getTotalRankCount()));
    }


    private List<String> translate(String name) {
        List<String> nameTranslate = extractGaBlock(name);

        if (nameTranslate.isEmpty()) {
            log.info("한자 번역 실패");
        }

        return nameTranslate;
    }

    public List<String> extractGaBlock(String name) {
        List<String> gaBlock = new ArrayList<>();
        boolean isBlock = false;
        String setName = "[" + name + "]";

        try (InputStream is = new ClassPathResource(TRANSLATE_FILE_NAME).getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(setName)) {
                    isBlock = true;
                    continue;
                }
                if (isBlock && line.trim().startsWith("[")) {
                    break;
                }

                if (isBlock) {
                    String[] parts = line.split("=");

                    if (parts.length > 1) {
                        // '=' 뒤의 부분에서 첫 번째 ',' 기준으로만 가져오기
                        String meaning = parts[1].split(",")[0].trim();

                        // '=' 앞의 부분과 결합하여 저장
                        gaBlock.add(parts[0] + "=" + meaning);
                    }
                    //gaBlock.add(line.trim());
                }
            }
        } catch (IOException e) {
            log.error("파일 읽기 오류", e);
        }

        return gaBlock;
    }
}
