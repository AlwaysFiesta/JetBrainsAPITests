package org.example.licenseapitesting.homework.suites;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.licenseapitesting.homework.domain.AssignFromTeamRequest;
import org.example.licenseapitesting.homework.domain.AssignLicenseRequest;
import org.example.licenseapitesting.homework.domain.AssigneeContactRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.example.licenseapitesting.homework.data.TestConstants.*;
import static org.example.licenseapitesting.homework.utils.Utils.*;
import static org.hamcrest.Matchers.equalTo;


public class AssignLicensesTest extends BaseTest {

    @BeforeEach
    public void resetLicenses() {
        //TODO: make same license available to assign in tests.
    }

    @Disabled
    @Test
    public void assignAvailableLicense_ToExistingAdminUserbyLicenceID_Success() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignAvailableLicense_ToNewUser_Success() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void AssignAvailableLicense_userNameNotAsInProfile_Success() throws IOException {
        AssigneeContactRequest contact = getExistingUser();
        contact.setFirstName("Flora");
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(contact)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void AssignAvailableLicense_disposableEmail_ErrorInvalidEmail() throws IOException {
        AssigneeContactRequest contact = getExistingUser();
        contact.setEmail(DISPOSABLE_EMAIL);
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(contact)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void AssignAvailableLicense_invalidFormatEmail_ErrorInvalidEmail() throws IOException {
        AssigneeContactRequest contact = getExistingUser();
        contact.setEmail("test.com");
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(contact)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignAvailableLicense_NoUserProvided_ErrorBadRequest() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignAvailableLicense_searchByTeamAndProduct_Success() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(AVAILABLE_TEAM_1)
                        .productCode(AVAILABLE_PRODUCTCODE)
                        .build())
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(200);
    }

    @Test
    public void assignLicense_searchByUnknownTeam_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(UNKNOWN_TEAM)
                        .productCode(AVAILABLE_PRODUCTCODE)
                        .build())
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignLicense_searchByTeam_UnavailableExistingProduct_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(AVAILABLE_TEAM_1)
                        .productCode(UNAVAILABLE_PRODUCTCODE)
                        .build())
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignLicense_searchByTeam_UnknownProduct_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .license(AssignFromTeamRequest.builder()
                        .team(AVAILABLE_TEAM_1)
                        .productCode(UNKNOWN_PRODUCTCODE)
                        .build())
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignLicense_noSearchParams_ErrorMissingField() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .contact(getRandomUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignExistingExpiredLicense_ErrorLicenseUnavailable() throws IOException {
        //Just choose one of the existing in my test team.

        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(UNAVAILABLE_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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
    public void assignExistingAssignedLicense_ErrorLicenseUnavailable() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(AVAILABLE_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(200);

        RestAssured.given(requestSpecificationOrgAdmin)
                .contentType(ContentType.JSON)
                .body(assignLicenseRequest)
                .log().all()
                .when()
                .post(ASSIGN_LICENSE_PATH)
                .then()
                .statusCode(400)
                .body("code", equalTo("LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN"))
                .body("description", equalTo("ALLOCATED"));    }

    @Test
    public void assignNonExistingLicense_ErrorNotFound() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(UNKNOWN_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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

    @Disabled("Not Implemented")
    @Test
    public void assignDuplicatedProductToUser_ErrorNotFound() throws IOException {
        //TODO assign 2 licenses for the same product to the same user
    }

    @Disabled("Not Implemented")
    @Test
    public void assignDifferentProductsToUser_ErrorNotFound() throws IOException {
        //TODO assign 2 licenses for the same product to the same user
    }


    @Test
    public void assigAvailableLicense_withIViewerRole_Error() throws IOException {
        AssignLicenseRequest assignLicenseRequest = AssignLicenseRequest.builder()
                .licenseId(UNKNOWN_LICENSEID)
                .contact(getExistingUser())
                .sendEmail(false)
                .build();

        RestAssured.given(requestSpecificationOrgAdmin)
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



    //TODO basic format test: past team id not as int32

}