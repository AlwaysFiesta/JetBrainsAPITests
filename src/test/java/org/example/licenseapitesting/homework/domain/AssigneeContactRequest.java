package org.example.licenseapitesting.homework.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeContactRequest {
    private String firstName;
    private String lastName;
    private String email;
}