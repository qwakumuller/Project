package com.example.reimbursement.Model;

import com.example.reimbursement.Utils.Status;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Reimbursement {

    private int employeeId;
    @Id
    private int reimbursementId;
    @NonNull
    private long reimbursementAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date reimbursementCreatedDate;
    private String assignedManager;
    private String purpose;
    @Enumerated(EnumType.STRING)
    private Status status;


}
