package com.example.reimbursement.Model;

import com.example.reimbursement.Utils.Roles;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "COMPANY")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    private int employeeId;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String userName;
    @NonNull
    private String emailAddress;
    @NonNull
    private String password;
    private Boolean isEmailVerified;
    @Enumerated(EnumType.STRING)
    private  Roles role;




}
