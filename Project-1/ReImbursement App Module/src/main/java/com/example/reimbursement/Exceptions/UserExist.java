package com.example.reimbursement.Exceptions;

public class UserExist extends RuntimeException{
    public UserExist(){
        super("User Already Exist In The Database");
    }
}
