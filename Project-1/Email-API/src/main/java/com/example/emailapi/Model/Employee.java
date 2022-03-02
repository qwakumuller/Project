package com.example.emailapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;







@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;
    private String password;
    private  Roles role;

}
