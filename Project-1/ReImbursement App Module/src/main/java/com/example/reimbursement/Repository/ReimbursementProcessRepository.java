package com.example.reimbursement.Repository;

import com.example.reimbursement.Model.ReimbursementProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReimbursementProcessRepository extends JpaRepository<ReimbursementProcess, Integer> {
    List<ReimbursementProcess> findReimbursementProcessByReimbursementId(int reimburseId);
}
