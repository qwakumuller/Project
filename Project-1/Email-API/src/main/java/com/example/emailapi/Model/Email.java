package com.example.emailapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private int emailId;
    private Date dateEmailSent;
    private String emailAddress;
    private boolean isSuccessful;

}
