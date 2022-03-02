package com.example.reimbursement.Exceptions;

import com.example.reimbursement.Model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(UserExist.class)
    public ResponseEntity userExist() {
     HttpResponse httpResponse = new HttpResponse(HttpStatus.NOT_FOUND, "Please Change Your Username", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(httpResponse);
    }

    @ExceptionHandler(VerifyAccount.class)
    public ResponseEntity<HttpResponse> verifyAccount() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED, "Please Verify Your Account", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(httpResponse,HttpStatus.UNAUTHORIZED);

    }


    @ExceptionHandler(ServerDown.class)
    public ResponseEntity serverDown() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.FAILED_DEPENDENCY, "Server Down", HttpStatus.FAILED_DEPENDENCY.value());
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(httpResponse);
    }

    @ExceptionHandler(JWTExpired.class)
    public ResponseEntity tokenExpired() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.FAILED_DEPENDENCY, "Token has Expired", HttpStatus.FAILED_DEPENDENCY.value());
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(httpResponse);
    }
}
