package com.bivgroup.constant;

import java.util.Set;

public class Constants {

    public static final String DATE_PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final String SUCCESSFULLY_PROCESSED_STATUS = "Обработан успешно";

    public static final String FOOTER_TEXT = "Business intelligence vision, 2025";

    public static final Long SUCCESS_STATUS_CODE = 0L;

    public static String USER_LOGIN_ATTRIBUTE = "userLogin";
    public static String AUTH_TOKEN_ATTRIBUTE = "authToken";
    public static String INSURER_ID_ATTRIBUTE = "insurerId";
    public static Set<String> SESSION_ATTRIBUTES = Set.of(
            USER_LOGIN_ATTRIBUTE,
            AUTH_TOKEN_ATTRIBUTE,
            INSURER_ID_ATTRIBUTE
    );

    private Constants() {}
}
