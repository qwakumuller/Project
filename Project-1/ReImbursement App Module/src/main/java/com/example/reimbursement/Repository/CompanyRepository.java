package com.example.reimbursement.Repository;

import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Utils.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Employee, Integer> {
    Employee findEmployeeByEmailAddress(String emailAddress);
    List<Employee> findEmployeeByRole(Roles role);
    Employee findEmployeeByFirstName(String firstname);

   Optional <Employee> findEmployeeByUserName(String username);

}
