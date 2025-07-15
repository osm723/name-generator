package com.oh.name_generator.domain.fortune.service;

import com.oh.name_generator.domain.fortune.dto.FortuneRequestDto;
import com.oh.name_generator.domain.fortune.dto.FortuneResponseDto;
import com.oh.name_generator.domain.fortune.dto.FortuneSaveRequestDto;
import com.oh.name_generator.domain.fortune.entity.Fortune;
import com.oh.name_generator.domain.fortune.repository.FortuneRepository;
import com.oh.name_generator.global.common.utils.CookieUtils;
import com.oh.name_generator.global.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.oh.name_generator.global.common.consts.Constants.Cookie.FORTUNE_COOKIE;
import static com.oh.name_generator.global.common.consts.Constants.Messages.*;
import static com.oh.name_generator.global.common.consts.Constants.Validation.MAX_FORTUNE_SIZE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FortuneServiceImpl implements FortuneService {

    private final FortuneRepository fortuneRepository;

    private final CookieUtils cookieUtils;

    @Override
    public FortuneResponseDto generateFortune(FortuneRequestDto fortuneRequestDto) {
        Long generationLongNum = getGenerationLongNum(fortuneRequestDto.getInputWord(), fortuneRequestDto.getBirth());
        Fortune fortune = fortuneRepository.findById(generationLongNum).orElseThrow(() -> new BusinessException(FORTUNE_GENERATION_FAIL));
        return new FortuneResponseDto(fortuneRequestDto.getInputWord(), fortuneRequestDto.getBirth(), fortune.getSentence(), LocalDate.now());
    }

    @Override
    public void saveFortune(FortuneSaveRequestDto fortuneSaveRequestDto, HttpServletRequest request, HttpServletResponse response) {
        if (fortuneSaveRequestDto == null) {
            throw new BusinessException(NOT_FOUND_REQUEST_FORTUNE);
        }
        cookieUtils.setCookie(FORTUNE_COOKIE, fortuneSaveRequestDto, request, response);
    }

    @Override
    public List<FortuneResponseDto> getSavedFortunes(HttpServletRequest request) {
        return cookieUtils.getCookie(FORTUNE_COOKIE, request);
    }

    @Override
    public void deleteSavedFortunes(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.removeCookie(FORTUNE_COOKIE, request, response);
    }

    /**
     * 단어와 현재시간(LocalDate)의 hashCode를 이용해 1~FORTUNE_MAX_NUM 까지의 숫자 생성
     * getGenerationNum
     * @param word
     * @param birth
     * @return String
     */
    private Long getGenerationLongNum(String word, Integer birth) {
        return (long) (Math.abs((word.hashCode() + LocalDate.now().hashCode() + birth) % MAX_FORTUNE_SIZE) + 1);
    }
}
