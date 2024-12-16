package com.KMA.BookingCare.common;

import com.KMA.BookingCare.Dto.SignalMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-webrtc
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 28/12/20
 * Time: 17.41
 */
public class JsonUtils {
    // Jackson JSON converter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static SignalMessage getObject(final String message) throws Exception {
        return objectMapper.readValue(message, SignalMessage.class);
    }

    public static String getString(final SignalMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}
