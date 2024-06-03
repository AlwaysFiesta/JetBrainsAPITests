package org.example.licenseapitesting.homework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.licenseapitesting.homework.domain.AssigneeContactRequest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class JsonUtils {
    public static Map<String, AssigneeContactRequest> readContactsFromConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                Paths.get("src/test/resources/Users.json").toFile(),
                new TypeReference<Map<String, AssigneeContactRequest>>() {}
        );
    }
}
