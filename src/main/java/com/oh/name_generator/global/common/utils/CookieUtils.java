package com.oh.name_generator.global.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CookieUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();


    public <T> void setCookie(String cookieName, T saveValue, HttpServletRequest request, HttpServletResponse response) {
        List<T> savedCookies = getCookie(cookieName, request);
        savedCookies.add(savedCookies.size(), saveValue);

        try {
            String json = objectMapper.writeValueAsString(savedCookies);
            String encodedValue = URLEncoder.encode(json, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie(cookieName, encodedValue);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간 설정 7일
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> getCookie(String cookieName, HttpServletRequest request) {
        List<T> savedCookies = new ArrayList<>();
        if (request.getCookies() != null) {
            Cookie findCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
            if (findCookie != null) {
                try {
                    savedCookies = objectMapper.readValue(URLDecoder.decode(findCookie.getValue()), List.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return savedCookies;
    }

    public void removeCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
        Cookie savedCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (savedCookie != null) {
            savedCookie.setPath("/");
            savedCookie.setMaxAge(0);
            response.addCookie(savedCookie);
        }
    }
}
