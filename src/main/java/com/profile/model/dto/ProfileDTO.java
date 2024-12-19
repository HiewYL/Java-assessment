package com.profile.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDTO implements Serializable {

    private Long id;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private String homeAddress1;
    private String homeAddress2;
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email address is required")
    private String emailAddress;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number must contain only numeric characters only.")
    private String phoneNumber;
    private String statusCode;
    private OffsetDateTime createdTime;
    private int version;
}
