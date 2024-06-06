package org.example.licenseapitesting.homework.tests;


import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.licenseapitesting.homework.domain.AssignFromTeamRequest;
import org.example.licenseapitesting.homework.domain.AssignLicenseRequest;
import org.example.licenseapitesting.homework.domain.AssigneeContactRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.example.licenseapitesting.homework.data.TestConstants.*;
import static org.example.licenseapitesting.homework.utils.Utils.*;
import static org.hamcrest.Matchers.equalTo;


@Epic("Assign licenses API tests")
public class AssignLicensesTest extends BaseTest {

    @BeforeEach
    public void resetLicenses() {
        //TODO: make same license available to assign in tests or some generator of licenses.
    }



    @Test
    @DisplayName("Assign available license to existing admin user. Search license by license ID")
    @Severity(SeverityLevel.BLOCKER) //just for fun, yeah, my blocker does not work :)
    public void assignAvailableLicense_ToExistingAdminUserbyLicenceID_Success() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(200);
    }


    @Disabled
    @Test
    @DisplayName("Assign available license to new user")
    public void assignAvailableLicense_ToNewUser_Success() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(200);
    }


    @Disabled
    @Test
    @DisplayName("User name not matching his JetBrains profile")
    public void AssignAvailableLicense_userNameNotAsInProfile_Success() throws IOException {
        AssigneeContactRequest contact = getExistingUser();
        contact.setFirstName("Flora");
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(contact)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .log().all()
                .statusCode(200);
    }


    @Test
    @DisplayName("User with valid email from known disposable email service")
    public void AssignAvailableLicense_disposableEmail_ErrorInvalidEmail() throws IOException {
        AssigneeContactRequest contact = getExistingUser();
        contact.setEmail(DISPOSABLE_EMAIL);
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(contact)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post("/customer/licenses/assign")
                .then()
                .log().all()
                .statusCode(400)
                .body("code", equalTo("INVALID_CONTACT_EMAIL"))
                .body("description", equalTo(DISPOSABLE_EMAIL));
    }


    @Test
    @DisplayName("Invalid email")
    public void AssignAvailableLicense_invalidFormatEmail_ErrorInvalidEmail() throws IOException {
        AssigneeContactRequest contact = getExistingUser();
        contact.setEmail("test.com");
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(contact)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post("/customer/licenses/assign")
                .then()
                .log().all()
                .statusCode(400)
                .body("code", equalTo("INVALID_CONTACT_EMAIL"))
                .body("description", equalTo("test.com"));
    }



    @Test
    @DisplayName("User is not provided")
    public void assignAvailableLicense_NoUserProvided_ErrorBadRequest() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(400);
    }

    @Disabled
    @Test
    @DisplayName("Assign available license to existing admin user. Search license by team and product id.")
    public void assignAvailableLicense_searchByTeamAndProduct_Success() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(AVAILABLE_TEAM_1)
                        .productCode(AVAILABLE_PRODUCTCODE)
                        .build())
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(200);
    }


    @Test
    @DisplayName("Unknown team id")
    public void assignLicense_searchByUnknownTeam_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(UNKNOWN_TEAM)
                        .productCode(AVAILABLE_PRODUCTCODE)
                        .build())
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(404)
                .body("code", equalTo("TEAM_NOT_FOUND"))
                .body("description", equalTo(String.valueOf(UNKNOWN_TEAM)));
    }


    @Test
    @DisplayName("Unavailable product code. Product exists in system.")
    public void assignLicense_searchByTeam_UnavailableExistingProduct_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(AVAILABLE_TEAM_1)
                        .productCode(UNAVAILABLE_PRODUCTCODE)
                        .build())
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .log().all()
                .statusCode(400)
                .body("code", equalTo("NO_AVAILABLE_LICENSE_TO_ASSIGN"))
                .body("description", equalTo("No available license found to assign in the team " + AVAILABLE_TEAM_1 + " with product " + UNAVAILABLE_PRODUCTCODE));
    }


    @Test
    @DisplayName("Unknown product code")
    public void assignLicense_searchByTeam_UnknownProduct_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(AVAILABLE_TEAM_1)
                        .productCode(UNKNOWN_PRODUCTCODE)
                        .build())
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .log().all()
                .statusCode(404)
                .body("code", equalTo("PRODUCT_NOT_FOUND"))
                .body("description", equalTo(UNKNOWN_PRODUCTCODE));
    }


    @Test
    @DisplayName("No licenses parameters provided")
    public void assignLicense_noSearchParams_ErrorMissingField() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(400)
                .body("code", equalTo("MISSING_FIELD"))
                .body("description", equalTo("Either licenseId or license must be provided"));
    }


    @Test
    @DisplayName("Assign existing not available license (expired)")
    public void assignExistingExpiredLicense_ErrorLicenseUnavailable() throws IOException {
        //Just choose one of the existing in my test team.

        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(UNAVAILABLE_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(400)
                .body("code", equalTo("LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN"))
                .body("description", equalTo("EXPIRED_WITHOUT_FALLBACK"));

    }


    @Disabled
    @Test
    @DisplayName("Assign existing not available license (already assigned)")
    public void assignExistingAssignedLicense_ErrorLicenseUnavailable() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(200);

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(400)
                .body("code", equalTo("LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN"))
                .body("description", equalTo("ALLOCATED"));
    }


    @Test
    @DisplayName("Assign unknown license id")
    public void assignNonExistingLicense_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(UNKNOWN_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .log().all()
                .statusCode(404)
                .body("code", equalTo("LICENSE_NOT_FOUND"))
                .body("description", equalTo(UNKNOWN_LICENSEID));

    }

    @Test
    @Disabled("Not implemented")
    @DisplayName("Assign 2 licenses with the same product code to user")
    public void assignDuplicatedProducIDtToUser_Success() throws IOException {

    }

    @Test
    @Disabled("Not implemented")
    @DisplayName("Assign 2 licenses with different product code to user")
    public void assignDifferentProductsToUser_Success() throws IOException {

    }


    //TODO check with devs when 403 forbidden here is thrown
    //TODO basic format tests: past team id not as int32 etc.

}