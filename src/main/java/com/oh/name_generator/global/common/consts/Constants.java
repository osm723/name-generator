package com.oh.name_generator.global.common.consts;

public final class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final class Cookie {
        public static final String NAME_COOKIE = "saved_names";
        public static final String FORTUNE_COOKIE = "saved_fortune";
        public static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7일
        public static final String COOKIE_PATH = "/";

        private Cookie() {}
    }

    public static final class Validation {
        public static final int MAX_NAME_SIZE = 10;
        public static final int MAX_FORTUNE_SIZE = 100;
        public static final int MAX_NAME_LENGTH = 2;
        public static final int MIN_BIRTH_YEAR = 1900_01_01;
        public static final int MAX_BIRTH_YEAR = 2030_12_31;

        private Validation() {}
    }

    public static final class Api {
        public static final String API_V1_PREFIX = "/api/v1";
        private Api() {}
    }

    public static final class Messages {
        public static final String NAME_GENERATION_SUCCESS = "이름이 성공적으로 생성되었습니다.";
        public static final String FORTUNE_GENERATION_SUCCESS = "운세가 성공적으로 생성되었습니다.";
        public static final String SAVE_SUCCESS = "성공적으로 저장되었습니다.";
        public static final String DELETE_SUCCESS = "성공적으로 삭제되었습니다.";
        public static final String NOT_FOUND_REQUEST_NAME = "저장할 이름이 없습니다.";
        public static final String NOT_FOUND_REQUEST_FORTUNE = "저장할 운세가 없습니다.";
        public static final String NOT_FOUND_NAME_LIST = "이름 목록이 존재하지 않습니다.";
        public static final String NAME_GENERATION_FAIL = "이름 생성중 문제가 발생하여 생성에 실패했습니다.";
        public static final String FORTUNE_GENERATION_FAIL = "운세 생성중 문제가 발생하여 생성에 실패했습니다.";

        // Error Messages
        public static final String INVALID_INPUT = "입력값이 올바르지 않습니다.";
        public static final String NAME_GENERATION_FAILED = "이름 생성 중 오류가 발생했습니다.";
        public static final String FORTUNE_GENERATION_FAILED = "운세 생성 중 오류가 발생했습니다.";
        public static final String EXTERNAL_API_ERROR = "외부 API 호출 중 오류가 발생했습니다.";

        private Messages() {}
    }

    public static final class FileDir {
        public static final String TRANSLATE_FILE_NAME = "translate/hanja.txt";
    }
}
