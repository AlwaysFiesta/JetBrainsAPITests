package org.example.licenseapitesting.homework.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignLicenseRequest {
    private String licenseId;
    private AssignFromTeamRequest license;
    private AssigneeContactRequest contact;
    private Boolean sendEmail;
    private Boolean includeOfflineActivationCode;
}