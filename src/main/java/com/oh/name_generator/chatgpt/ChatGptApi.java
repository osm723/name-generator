package com.oh.name_generator.chatgpt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oh.name_generator.name.dto.NameRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ChatGptApi {

    @Value("${api.gpt.key}")
    private String GPT_API_KEY;

    @Value("${api.gpt.url}")
    private String GPT_API_URL;

    /**
     * generationNamesWithGpt
     * gpt를 이용한 이름 생성
     * @param nameRequestDto
     * @return
     */
    public List<String> generationNamesWithGpt(NameRequestDto nameRequestDto) {
        String requestMessage = setRequestMessage(nameRequestDto);

        try {
            HttpResponse<String> response = gptApiCall(requestMessage);

            if (response.statusCode() == 200) {
                log.info("Response: {}", response.body());
                return responseParsing(response, nameRequestDto.getLastName());
            } else {
                log.error("ErrorCode: {}, ErrorMessage: {} ", response.statusCode(),response.body());
                return List.of("실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("실패");
        }
    }

    /**
     * setRequestMessage
     * gpt에 요청할 메시지
     * @param nameRequestDto
     * @return requestMessage
     */
    private static String setRequestMessage(NameRequestDto nameRequestDto) {
        String requestMessage = "두글자 한국 아이 이름 10개만 만들어줘. 영문명이나 한자 풀이, 뜻은 필요없어 ";

        if (nameRequestDto != null) {
            if (!nameRequestDto.getGender().isBlank()) {
                requestMessage = nameRequestDto.getGender() + "자 " + requestMessage;
            }

            if (!nameRequestDto.getFirstName().isBlank()) {
                requestMessage = nameRequestDto.getFirstName() + " 로 시작하는 " + requestMessage;
            }

            if (!nameRequestDto.getSecondName().isBlank()) {
                requestMessage = nameRequestDto.getSecondName() + " 로 끝나는 " + requestMessage;
            }
        }

        log.info("requestMessage={}",requestMessage);

        return requestMessage;
    }

    /**
     * responseParsing
     * gpt로부터 응답값 파싱
     * @param response
     * @param paramLastName
     * @return names
     * @throws JsonProcessingException
     */
    private static List<String> responseParsing(HttpResponse<String> response, @NotNull String paramLastName) throws JsonProcessingException {
        String lastName = paramLastName == null ? "" : paramLastName;
        String responseBody = response.body();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode messageNode = rootNode.path("choices").get(0).path("message");
        String content = messageNode.path("content").asText();
        String[] splitNames = content.split("\n");
        List<String> names = new ArrayList<>();

        for (String splitName : splitNames) {
            // 항목에서 숫자와 마침표를 제거하고, 이름만 추가
            String name = splitName.split("\\.")[1].trim();  // 숫자와 마침표를 제거
            names.add(lastName + name);
        }

        return names;
    }

    /**
     * gptApiCall
     * gpt api 설정 및 호출
     * @param requestMessage
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    private HttpResponse<String> gptApiCall(String requestMessage) throws IOException, InterruptedException {
        String requestData = setRequestData(requestMessage).toString();

        // HTTP 클라이언트 및 요청 생성
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GPT_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + GPT_API_KEY)  // 인증 헤더
                .POST(HttpRequest.BodyPublishers.ofString(requestData))
                .build();

        // API 호출
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * setRequestData
     * gpt에 요청할 데이터 설정
     * @param requestMessage
     * @return
     */
    private static ObjectNode setRequestData(String requestMessage) {
        ObjectMapper objectMapper = new ObjectMapper();

        // body 생성
        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("model", "gpt-3.5-turbo");
        jsonBody.put("max_tokens", 120);  // 생성할 최대 토큰 수

        // messages 배열 생성
        ArrayNode messages = objectMapper.createArrayNode();

        // userMessage 추가
        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", requestMessage);
        messages.add(userMessage);

        // messages 파라미터 추가
        jsonBody.set("messages", messages);
        return jsonBody;
    }


}
