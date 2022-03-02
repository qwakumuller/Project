package com.example.emailapi.Model;
import java.util.Date;


public class Reimbursement {

    private int employeeId;
    private int reimbursementId;
    private long reimbursementAmount;
    private Date reimbursementCreatedDate;
    private String assignedManager;
    private Boolean isEmailVerified;
    private Status status;

    public Reimbursement(int reimbursementId, long reimbursementAmount, Date reimbursementCreatedDate, Status status) {
        this.reimbursementId = reimbursementId;
        this.reimbursementAmount = reimbursementAmount;
        this.reimbursementCreatedDate = reimbursementCreatedDate;
        this.status = status;
    }

    public Reimbursement() {
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(int reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public long getReimbursementAmount() {
        return reimbursementAmount;
    }

    public void setReimbursementAmount(long reimbursementAmount) {
        this.reimbursementAmount = reimbursementAmount;
    }

    public Date getReimbursementCreatedDate() {
        return reimbursementCreatedDate;
    }

    public void setReimbursementCreatedDate(Date reimbursementCreatedDate) {
        this.reimbursementCreatedDate = reimbursementCreatedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
