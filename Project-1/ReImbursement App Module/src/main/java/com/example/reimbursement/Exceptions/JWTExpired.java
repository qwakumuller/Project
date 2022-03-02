package com.example.reimbursement.Exceptions;

public class JWTExpired extends RuntimeException{

    public JWTExpired(){
        super("Token has Expired, Please Log in");
    }
}
