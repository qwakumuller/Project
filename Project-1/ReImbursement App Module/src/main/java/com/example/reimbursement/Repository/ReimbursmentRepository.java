package com.example.reimbursement.Repository;

import com.example.reimbursement.Model.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReimbursmentRepository extends JpaRepository<Reimbursement, Integer> {
    Optional<Reimbursement> findReimbursementByReimbursementId(int reimburseId);
    List<Reimbursement> findReimbursementByEmployeeId(int employeeId);
}
