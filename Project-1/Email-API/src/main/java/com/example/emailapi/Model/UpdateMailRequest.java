package com.example.emailapi.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMailRequest {

private int reimbursementId;
private String email;
private Status status;
private long reimbursementAmount;
}
