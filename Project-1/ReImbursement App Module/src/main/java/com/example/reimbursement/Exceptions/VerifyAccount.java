package com.example.reimbursement.Exceptions;

public class VerifyAccount extends RuntimeException{
    public VerifyAccount(){
        super("Please Verify Your Account via Email Sent");
    }
}
