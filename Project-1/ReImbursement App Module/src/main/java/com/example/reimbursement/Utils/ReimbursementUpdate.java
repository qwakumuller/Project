package com.example.reimbursement.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReimbursementUpdate {
    private int reimburseId;
    @Enumerated(EnumType.STRING)
    private Status status;
}
