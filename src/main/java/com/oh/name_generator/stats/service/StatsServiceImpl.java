package com.oh.name_generator.stats.service;

import com.oh.name_generator.stats.dto.*;
import com.oh.name_generator.stats.entity.NameStats;
import com.oh.name_generator.stats.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    private final ModelMapper modelMapper;

    //@Value("${path.translate_file}")
    private static String translateFileName = "translate/hanja.txt";

    @Override
    public Page<StatsResponseDto> findAll(Pageable pageable) {
        Page<NameStats> nameStatsList = statsRepository.findAll(pageable);
        return nameStatsList.map(StatsResponseDto::new);
    }

    @Override
    public Page<StatsResponseDto> findByWhere(Pageable pageable, StatsRequestDto statsRequestDto) {
        Page<NameStats> names = statsRepository.findByWhere(pageable, modelMapper.map(statsRequestDto, StatsRequestCond.class));
        return transformStatsResponseDto(names);
    }

    @Override
    public StatsPopupResponseDto findPopupByNameAndYears(StatsPopupRequestDto statsPopupRequestDto) {
        NameStats name = statsRepository.findByNameAndYears(statsPopupRequestDto.getName(), statsPopupRequestDto.getYears());
        StatsPopupResponseDto statsPopupResponseDto = modelMapper.map(name, StatsPopupResponseDto.class);

        // 한자 찾기
        String translateName = statsPopupRequestDto.getName();
        statsPopupResponseDto.setFirstNameTranslate(translate(translateName.substring(0, 1)));
        statsPopupResponseDto.setSecondNameTranslate(translate(translateName.substring(1, 2)));

        return statsPopupResponseDto;
    }

    @Override
    public Map<Integer, Long> findYearCountByName(StatsPopupRequestDto statsPopupRequestDto) {
        return statsRepository.findYearCountByName(modelMapper.map(statsPopupRequestDto, StatsRequestCond.class));
    }

    @Override
    public Map<Integer, Integer> findYearRankByName(StatsPopupRequestDto statsPopupRequestDto) {
        return statsRepository.findYearRankByName(modelMapper.map(statsPopupRequestDto, StatsRequestCond.class));
    }

    private static Page<StatsPopupResponseDto> transformStatsPopupResponseDto(Page<NameStats> names) {
        return names.map(nameStats -> new StatsPopupResponseDto(
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

    private static Page<StatsResponseDto> transformStatsResponseDto(Page<NameStats> names) {
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

    public static List<String> extractGaBlock(String name) {
        List<String> gaBlock = new ArrayList<>();
        boolean isBlock = false;
        String setName = "[" + name + "]";

        try {
            InputStream is = new ClassPathResource(translateFileName).getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
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
            e.printStackTrace();
        }

        return gaBlock;
    }
}
