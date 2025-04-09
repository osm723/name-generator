package com.oh.name_generator.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ControllerAdvice
public class GlobalModelAttribute {

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("years", initYears());
    }

    /**
     * initYears
     * 연도 설정 값
     * @return years
     */
    //@ModelAttribute("years")
    public List<Integer> initYears() {
        List<Integer> years = IntStream.rangeClosed(2008, LocalDate.now().getYear()-1)
                .boxed()
                .collect(Collectors.toList());
        return years;
    }
}
