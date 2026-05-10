
package co.unicauca.frontend.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class JsonUtil {

    private static final ObjectMapper mapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    //private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        try {
            return mapper.readValue(
                    json,
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object obj) {

        try {

            return mapper.writeValueAsString(obj);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}