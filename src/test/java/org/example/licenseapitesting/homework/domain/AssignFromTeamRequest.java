package org.example.licenseapitesting.homework.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignFromTeamRequest {
    private Integer team;
    private String productCode;
}