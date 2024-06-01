package org.example.licenseapitesting.homework.utils;

import org.example.licenseapitesting.homework.domain.ApiToken;

public class TokenManager {
    public ApiToken getToken() {
        ApiToken token = new ApiToken();
        token.setXApiKey(System.getenv("X_API_KEY"));
        token.setXCustomerCode(System.getenv("X_CUSTOMER_CODE"));
        return token;
    }
}
