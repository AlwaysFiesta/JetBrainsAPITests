package org.example.licenseapitesting.homework.suites;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.licenseapitesting.homework.domain.ChangeTeamRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.example.licenseapitesting.homework.data.TestConstants.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeLicensesTeamTest extends BaseTest {

    //TODO check in code if license types like expired, Renewal stopped etc can somehow affect.

    @BeforeEach
    public void setUpTeams() {
        //TODO bring back teams licenses to where they were. For now done in every test
    }

    @Test
    public void transferOneAvailableLicense_toValidTeam_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST2)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID_TEAM_TEST1));
        ;

        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID_TEAM_TEST1));
        ;
    }


    @Test
    public void transferUnavailableLicense_ToValidTeam_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST2)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID_TEAM_TEST1));

        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID_TEAM_TEST1));
    }

    @Test
    public void transferSeveralLicenses_ToValidTeam_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID_TEAM_TEST1, AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST2)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(2))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID_TEAM_TEST1, AVAILABLE_LICENSEID_TEAM_TEST1));

        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNAVAILABLE_LICENSEID_TEAM_TEST1, AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(2))
                .body("licenseIds", Matchers.containsInAnyOrder(UNAVAILABLE_LICENSEID_TEAM_TEST1, AVAILABLE_LICENSEID_TEAM_TEST1));
    }

    @Test
    public void emptyLicenseListRequest_ToValidTeam_Success_EmptyLicenseIdListInResponse() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList()))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void transferUnknownLicense_ToValidTeam_Success_EmptyLicenseIdListInResponse() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(UNKNOWN_LICENSEID)))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void transferLisense_ToSameTeam_Success_EmptyLicenseIdListInResponse() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void transferLisense_ToUnknownTeam_ErrorUnknownTeam() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(UNKNOWN_TEAM)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void transferAvailableLicense_toLicenseServer_Success() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(AVAILABLE_LICENSE_SERVER)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID_TEAM_TEST1));

        ChangeTeamRequest changeTeamRequestBack = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST1)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(changeTeamRequestBack)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(200)
                .body("licenseIds", Matchers.hasSize(1))
                .body("licenseIds", Matchers.containsInAnyOrder(AVAILABLE_LICENSEID_TEAM_TEST1));
    }

    @Test
    public void transferAvailableLicense_fromLicenseServer_toValidTeam_Success() {
        //TODO: covered now in transferAvailableLicense_toLicenseServer_Success. Will be added separately after setting up @BeforeEach
    }

    @Test
    public void missedMandatoryLicenseIDsInRequest_Error() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .targetTeamId(UNKNOWN_TEAM)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void noTargetTeamIDInRequest_ErrorTeamNotFound() {
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void transferAvailableLicense_withViewerRole_Error(){
        ChangeTeamRequest changeTeamRequest = ChangeTeamRequest.builder()
                .licenseIds(new ArrayList<>(Arrays.asList(AVAILABLE_LICENSEID_TEAM_TEST1)))
                .targetTeamId(TEAM_TEST2)
                .build();

        RestAssured.given(requestSpecificationViewer)
                .contentType(ContentType.JSON)
                .body(changeTeamRequest)
                .log().all()
                .when()
                .post(CHANGE_LICENSE_TEAM_PATH)
                .then()
                .log().all()
                .statusCode(403)
                .body("code", equalTo("TOKEN_TYPE_MISMATCH"))
                .body("description", equalTo(String.valueOf("Changing team is not possible with a token that was generated for a specific team")));
    }



    @Disabled("Not Implemented yet")
    @Test
    public void transferAvailableLicense_withTeamAdminRole_Error(){
    //TODO. Based on transferAvailableLicense_withViewerRole_Error test this one will likely throw an error as well.
    }

    //TODO basic teamID type check, send String instead.
}
