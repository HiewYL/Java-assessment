package com.profile.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user_profile")
public class Profile {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "home_address_1")
    private String homeAddress1;
    @Column(name = "home_address_2")
    private String homeAddress2;
    @Column(name = "email")
    private String emailAddress;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "created_time")
    private OffsetDateTime createdTime;
    @Column(name = "sts_cd")
    private String statusCode;
    @Column(name = "ver_nbr")
    private int version = 0;
}
