package com.tomcatdevs.Accounts.entity;

import lombok.Data;

@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String designation;
    private String phone;
}
