package com.example.reimbursement.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {

    private HttpStatus httpStatus;
    private String message;
    private int httpStatusCode;

}
