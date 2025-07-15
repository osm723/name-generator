package com.oh.name_generator.external.chatgpt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oh.name_generator.domain.name.dto.NameRequestDto;
import com.oh.name_generator.global.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGptApiClient {

    @Value("${api.gpt.key}")
    private String apiKey;

    @Value("${api.gpt.url}")
    private String apiUrl;

    @Value("${api.gpt.timeout:30}")
    private int timeoutSeconds;

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public List<String> generationNamesWithGpt(NameRequestDto nameRequestDto) {
        try {
            String requestMessage = buildRequestMessage(nameRequestDto);
            HttpResponse<String> response = callGptApi(requestMessage);

            if (response.statusCode() == 200) {
                return parseResponse(response, nameRequestDto.getLastName());
            } else {
                log.error("GPT API 오류 - 상태코드: {}, 응답: {}", response.statusCode(), response.body());
                throw new ExternalApiException("이름 생성 서비스에 일시적인 문제가 발생했습니다.");
            }
        } catch (IOException e) {
            log.error("GPT API 네트워크 오류: {}", e.getMessage(), e);
            throw new ExternalApiException("네트워크 연결에 문제가 발생했습니다.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("GPT API 호출 중단: {}", e.getMessage(), e);
            throw new ExternalApiException("요청이 중단되었습니다.", e);
        } catch (Exception e) {
            log.error("GPT API 예상치 못한 오류: {}", e.getMessage(), e);
            throw new ExternalApiException("이름 생성 중 오류가 발생했습니다.", e);
        }
    }

    private String buildRequestMessage(NameRequestDto nameRequestDto) {
        StringBuilder message = new StringBuilder("두글자 한국 아이 이름 10개만 만들어줘. 영문명이나 한자 풀이, 뜻은 필요없어 ");

        if (nameRequestDto.getGender() != null && !nameRequestDto.getGender().isBlank()) {
            String genderText = "M".equals(nameRequestDto.getGender()) ? "남자" : "여자";
            message.insert(0, genderText + "아 ");
        }

        if (nameRequestDto.getFirstName() != null && !nameRequestDto.getFirstName().isBlank()) {
            message.insert(message.indexOf("두글자"), nameRequestDto.getFirstName() + "로 시작하는 ");
        }

        if (nameRequestDto.getSecondName() != null && !nameRequestDto.getSecondName().isBlank()) {
            message.insert(message.indexOf("두글자"), nameRequestDto.getSecondName() + "로 끝나는 ");
        }

        log.info("GPT 요청 메시지: {}", message);
        return message.toString();
    }

    private HttpResponse<String> callGptApi(String requestMessage) throws IOException, InterruptedException {
        ObjectNode requestData = buildRequestData(requestMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<String> parseResponse(HttpResponse<String> response, String lastName)
            throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode messageNode = rootNode.path("choices").get(0).path("message");
        String content = messageNode.path("content").asText();

        String[] splitNames = content.split("\n");
        List<String> names = new ArrayList<>();
        String prefix = lastName != null ? lastName : "";

        for (String splitName : splitNames) {
            if (splitName.contains(".")) {
                String[] parts = splitName.split("\\.", 2);
                if (parts.length > 1) {
                    String name = parts[1].trim();
                    names.add(prefix + name);
                }
            }
        }

        return names;
    }

    private ObjectNode buildRequestData(String requestMessage) {
        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("model", "gpt-3.5-turbo");
        jsonBody.put("max_tokens", 120);

        ArrayNode messages = objectMapper.createArrayNode();
        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", requestMessage);
        messages.add(userMessage);

        jsonBody.set("messages", messages);
        return jsonBody;
    }


}
