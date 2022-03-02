package com.example.reimbursement.Controller;

import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Service.CompanyService;
import com.example.reimbursement.Utils.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employee")
@CrossOrigin(origins = "http://localhost:3030")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    private static Logger logger = LoggerFactory.getLogger(CompanyController.class);


    @PostMapping(value = "/createEmployee", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createEmployee(@RequestBody EmployeeRequest employeeRequest){
       Employee employee =  companyService.saveEmployee(employeeRequest);
       logger.info("Employee Account has been created");
        return  ResponseEntity.status(HttpStatus.OK).body(
                employee.getUserName() != null ? "Employee Account Successfully Created" : "Employee Account Was Not Created"
        );
    }

    @PostMapping(value = "/createManager", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createManager(@RequestBody EmployeeRequest employeeRequest){
        Employee employee = companyService.saveManager(employeeRequest);
        logger.info("Manager Account has been created");
        return  ResponseEntity.status(HttpStatus.OK).body(
                employee.getUserName() != null ? "Manager Account Successfully Created" : "Manager Account Was Not Created"
        );
    }


    @PostMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllEmployee(){
        List<Employee> getAllEmployee = companyService.getAll();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(getAllEmployee);
    }

    @GetMapping("/verifyEmail/{employeeId}/{addToUrl}")
    public ResponseEntity verifyUserEmail(@PathVariable String employeeId){
       boolean verify =  companyService.verifyUser(employeeId);

       return ResponseEntity.status(HttpStatus.OK).body(
                verify == false ? "Error Occurred While Verify Your Account" : "Account is Successfully Verified"
        );
    }



}
