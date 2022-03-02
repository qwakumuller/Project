package com.example.reimbursement.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReimbursmentRequest {
    private long reimbursementAmount;
    private String purpose;


}
