package org.example.licenseapitesting.homework.tests;

import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.licenseapitesting.homework.domain.ChangeTeamRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.example.licenseapitesting.homework.data.TestConstants.*;
import static org.hamcrest.Matchers.equalTo;

@Epic("Transfer licenses tests")
public class ChangeLicensesTeamTest extends BaseTest {

    //TODO check in code if license types like expired, Renewal stopped etc can somehow affect.

    @BeforeEach
    public void setUpTeams() {
        //TODO bring back teams licenses to where they were. For now done in every test
    }

    @Test
    @DisplayName("Transfer available license to valid team")
    public void transferOneAvailableLicense_toValidTeam_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_2)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID));
        ;

        //TODO: fix later with some @BeforeEach setup probably
        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID));
        ;
    }


    @Test
    @DisplayName("Transfer unavailable license")
    public void transferUnavailableLicense_ToValidTeam_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_2)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID));

        //TODO: fix later with some @BeforeEach setup probably
        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID));
    }

    @Test
    @DisplayName("Transfer several licenses")
    public void transferSeveralLicenses_ToValidTeam_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID, AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_2)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(2))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID, AVAILABLE_LICENSEID));

        //TODO: fix later with some @BeforeEach setup probably
        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID, AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(2))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID, AVAILABLE_LICENSEID));
    }

    @Test
    @DisplayName("Empty list of licences for transfer")
    public void emptyLicenseListRequest_ToValidTeam_Success_EmptyLicenseIdListInResponse() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList()))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.empty());
    }
    @Test
    @DisplayName("Transfer unknown license")
    public void transferUnknownLicense_ToValidTeam_Success_EmptyLicenseIdListInResponse() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNKNOWN_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.empty());
    }

    @Test
    @DisplayName("Transfer license to the same team")
    public void transferLisense_ToSameTeam_Success_EmptyLicenseIdListInResponse() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.empty());
    }

    @Test
    @DisplayName("Transfer license to unknown team")
    public void transferLisense_ToUnknownTeam_ErrorUnknownTeam() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .targetTeamId(UNKNOWN_TEAM)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(404)
                .body("code", equalTo("TEAM_NOT_FOUND"))
                .body("description", equalTo(String.valueOf(UNKNOWN_TEAM)));
    }

    @Test
    @DisplayName("Transfer available license to license server")
    public void transferAvailableLicense_toLicenseServer_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_LICENSE_SERVER)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID));

        //TODO: fix later with some @BeforeEach setup probably
        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .targetTeamId(AVAILABLE_TEAM_1)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID));
    }

    @Test
    @DisplayName("Transfer available license from license server")
    @Disabled("Covered by transferAvailableLicense_toLicenseServer_Success for now")
    public void transferAvailableLicense_fromLicenseServer_toValidTeam_Success() {
        //TODO: covered now in transferAvailableLicense_toLicenseServer_Success. Will be added separately after setting up @BeforeEach
    }

    @Test
    @DisplayName("No licenseIDs in request")
    public void missedMandatoryLicenseIDsInRequest_Error() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .targetTeamId(UNKNOWN_TEAM)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("No targetTeamID in request")
    public void noTargetTeamIDInRequest_ErrorTeamNotFound() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID)))
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(404)
                .body("code", equalTo("TEAM_NOT_FOUND"))
                .body("description", equalTo(String.valueOf(UNKNOWN_TEAM)));
    }

    //TODO basic teamID type check, send String instead.
}
