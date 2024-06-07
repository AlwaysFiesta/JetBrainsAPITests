package org.example.licenseapitesting.homework.utils;

import org.example.licenseapitesting.homework.domain.ApiToken;

public class TokenManager {
    public ApiToken getToken(String xApiKeyVariable) {
        ApiToken token = new ApiToken();
        token.setXApiKey(System.getenv(xApiKeyVariable));
        token.setXCustomerCode(System.getenv("X_CUSTOMER_CODE"));
        return token;
    }
}
