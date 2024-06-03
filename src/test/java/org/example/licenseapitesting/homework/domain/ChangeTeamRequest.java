package org.example.licenseapitesting.homework.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTeamRequest {
    private Integer targetTeamId;
    private List<String> licenseIds;
}