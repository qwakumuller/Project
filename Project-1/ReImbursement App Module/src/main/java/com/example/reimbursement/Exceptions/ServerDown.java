package com.example.reimbursement.Exceptions;

public class ServerDown extends RuntimeException{

    public ServerDown(){
        super("Server is down while Connecting to it");
    }
}
