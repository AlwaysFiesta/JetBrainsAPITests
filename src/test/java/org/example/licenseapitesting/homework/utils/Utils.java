package org.example.licenseapitesting.homework.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.licenseapitesting.homework.domain.AssigneeContactRequest;

import java.io.IOException;

public class Utils {
    public static AssigneeContactRequest getRandomUser() {
        return AssigneeContactRequest.builder()
                .firstName(RandomStringUtils.randomAlphabetic(10))
                .lastName(RandomStringUtils.randomAlphabetic(10))
                .email(RandomStringUtils.randomAlphanumeric(15, 20) + "@" + RandomStringUtils.randomAlphabetic(7) + ".com")
                .build();
    }

    public static AssigneeContactRequest getExistingUser() throws IOException {
        return JsonUtils.readContactsFromConfig().get("ExistingUser");
    }
}
