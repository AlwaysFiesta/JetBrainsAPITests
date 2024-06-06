package org.example.licenseapitesting.homework.tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.licenseapitesting.homework.domain.ApiToken;
import org.example.licenseapitesting.homework.utils.TokenManager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

public class BaseTest {

    public static final String BASE_URI = "https://account.jetbrains.com/api/v1";
    private static ApiToken token = new TokenManager().getToken();
    protected static RequestSpecification requestSpecification;

    @BeforeAll
    static void setUp() {
        // to Ignore null values when serializing to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RestAssured.config = RestAssured.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> objectMapper
        ));

        requestSpecification = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", token.getXApiKey())
                .header("X-Customer-Code", token.getXCustomerCode());

        //add logging to allure reports
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterAll
    static void tearDown() {
        RestAssured.reset();
    }
}

