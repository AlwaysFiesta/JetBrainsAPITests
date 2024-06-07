package org.example.licenseapitesting.homework.suites;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static ApiToken tokenOrgAdmin = new TokenManager().getToken("X_API_KEY_ORG_ADMIN");
    private static ApiToken tokenTeamAdmin = new TokenManager().getToken("X_API_KEY_TEAM_ADMIN");
    private static ApiToken tokenViewer = new TokenManager().getToken("X_API_KEY_VIEWER");

    protected static RequestSpecification requestSpecificationOrgAdmin;

    protected static RequestSpecification requestSpecificationTeamAdmin;

    protected static RequestSpecification requestSpecificationViewer;

    @BeforeAll
    static void setUp() {
        // to Ignore null values when serializing to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RestAssured.config = RestAssured.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> objectMapper
        ));

        requestSpecificationOrgAdmin = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenOrgAdmin.getXApiKey())
                .header("X-Customer-Code", tokenOrgAdmin.getXCustomerCode());

        requestSpecificationTeamAdmin = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenTeamAdmin.getXApiKey())
                .header("X-Customer-Code", tokenTeamAdmin.getXCustomerCode());

        requestSpecificationViewer = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenViewer.getXApiKey())
                .header("X-Customer-Code", tokenViewer.getXCustomerCode());
    }

    @AfterAll
    static void tearDown() {
        RestAssured.reset();
    }
}

