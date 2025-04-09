package com.oh.name_generator.fortune.service;

import com.oh.name_generator.common.exception.BizException;
import com.oh.name_generator.fortune.dto.FortuneRequestDto;
import com.oh.name_generator.fortune.dto.FortuneResponseDto;
import com.oh.name_generator.fortune.entity.Fortune;
import com.oh.name_generator.fortune.repository.FortuneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FortuneServiceImpl implements FortuneService {

    private final FortuneRepository fortuneRepository;

    @Value("${max.fortune_size}")
    private int maxFortuneSize;


    @Override
    public FortuneResponseDto generationFortune(FortuneRequestDto fortuneRequestDto) {
        Long generationLongNum = getGenerationLongNum(fortuneRequestDto.getInputWord(), fortuneRequestDto.getBirth());
        Fortune fortune = fortuneRepository.findById(generationLongNum).orElseGet(() -> {
            throw new BizException("시스템 문제로 인해, 운세 생성에 실패했습니다.");
        });
        return new FortuneResponseDto(fortuneRequestDto.getInputWord(), fortuneRequestDto.getBirth(), fortune.getSentence(), LocalDate.now());
    }

    /**
     * 단어와 현재시간(LocalDate)의 hashCode를 이용해 1~FORTUNE_MAX_NUM 까지의 숫자 생성
     * getGenerationNum
     * @param word
     * @param birth
     * @return String
     */
    private Long getGenerationLongNum(String word, Integer birth) {
        return (long) (Math.abs((word.hashCode() + LocalDate.now().hashCode() + birth) % maxFortuneSize) + 1);
    }
}
