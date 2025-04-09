package com.oh.name_generator.name.controller;


import com.oh.name_generator.common.utils.CookieUtils;
import com.oh.name_generator.name.dto.NameRequestDto;
import com.oh.name_generator.name.dto.NameResponseDto;
import com.oh.name_generator.name.dto.NameSaveRequestDto;
import com.oh.name_generator.name.dto.NameSaveResponseDto;
import com.oh.name_generator.name.service.NameService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generation")
public class NameController {

    private final NameService nameService;

    private final CookieUtils cookieUtils;

    @Value("${cookie.name_cookie}")
    private String cookieName;

    /**
     * 이름 생성
     * createName
     * @param nameRequestDto
     * @return Map<String, Object>
     */
    @PostMapping("/name/create")
    public Map<String, Object> createName(@RequestBody @Valid NameRequestDto nameRequestDto) {
        List<NameResponseDto> names = nameService.createName(nameRequestDto);

        Map<String, Object> response = new HashMap<>();
        response.put("content", names);
        response.put("size", names.size());
        return response;
    }

    /**
     * 이름 조회
     * getName
     * @return Map<String, Object>
     */
    @GetMapping("/name")
    public Map<String, Object> getName(HttpServletRequest request) {
        List<NameSaveResponseDto> names = cookieUtils.getCookie(cookieName, request);

        Map<String, Object> response = new HashMap<>();
        response.put("content", names);
        response.put("count", names.size());
        return response;
    }

    /**
     * 이름 저장
     * saveName
     * @return ResponseEntity<Void>
     */
    @PostMapping("/name")
    public ResponseEntity<Void> saveName(@RequestBody List<NameSaveRequestDto> saveNames, HttpServletRequest request, HttpServletResponse response) {
        for (NameSaveRequestDto saveName : saveNames) {
            cookieUtils.setCookie(cookieName, saveName, request, response);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 이름 삭제
     * deleteName
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/name")
    public ResponseEntity<Void> deleteName(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.removeCookie(cookieName, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
