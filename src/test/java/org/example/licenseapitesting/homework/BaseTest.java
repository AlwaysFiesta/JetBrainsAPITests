package org.example.licenseapitesting.homework;

import org.example.licenseapitesting.homework.domain.ApiToken;
import org.example.licenseapitesting.homework.utils.TokenManager;

public class BaseTest {
    protected static ApiToken token = new TokenManager().getToken();

}
