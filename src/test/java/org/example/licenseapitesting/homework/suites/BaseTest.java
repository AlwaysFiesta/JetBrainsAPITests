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
    private static ApiToken tokenAdminTeamTest2 = new TokenManager().getToken("X_API_KEY_TEAM_ADMIN");
    private static ApiToken tokenViewerTeamTest2 = new TokenManager().getToken("X_API_KEY_VIEWER");

    protected static RequestSpecification requestSpecOrgAdmin;

    protected static RequestSpecification requestSpecAdminTeamTest2;

    protected static RequestSpecification requestSpecViewerTeamTest1;

    protected static RequestSpecification requestSpecInvalidToken;

    protected static RequestSpecification requestSpecInvalidCustomerCode;

    @BeforeAll
    static void setUp() {
        // to Ignore null values when serializing to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RestAssured.config = RestAssured.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> objectMapper
        ));

        requestSpecOrgAdmin = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenOrgAdmin.getXApiKey())
                .header("X-Customer-Code", tokenOrgAdmin.getXCustomerCode());

        requestSpecAdminTeamTest2 = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenAdminTeamTest2.getXApiKey())
                .header("X-Customer-Code", tokenAdminTeamTest2.getXCustomerCode());

        requestSpecViewerTeamTest1 = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenViewerTeamTest2.getXApiKey())
                .header("X-Customer-Code", tokenViewerTeamTest2.getXCustomerCode());

        requestSpecInvalidToken = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", "invalid")
                .header("X-Customer-Code",  tokenOrgAdmin.getXCustomerCode());

        requestSpecInvalidCustomerCode = RestAssured.given()
                .baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .header("X-Api-Key", tokenOrgAdmin.getXApiKey())
                .header("X-Customer-Code", "test");

    }

    @AfterAll
    static void tearDown() {
        RestAssured.reset();
    }
}

