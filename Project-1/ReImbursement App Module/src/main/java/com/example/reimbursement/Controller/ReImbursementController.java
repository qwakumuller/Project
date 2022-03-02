package com.example.reimbursement.Controller;

import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Model.Reimbursement;
import com.example.reimbursement.Service.ReImbursmentService;
import com.example.reimbursement.Utils.ProcessRequest;
import com.example.reimbursement.Utils.ReimbursmentRequest;
import com.example.reimbursement.Utils.updateReimbursement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3030")
@RequestMapping("api")
public class ReImbursementController {

    @Autowired
    private ReImbursmentService reImbursmentService;

    private static Logger logger = LoggerFactory.getLogger(ReImbursementController.class);


    /**
     *
     * @param reImbursmentRequest the request created by employee
     * @return a status of OK
     */
    @PostMapping("/reimburse")
    public ResponseEntity createReimbursement(@RequestBody ReimbursmentRequest reImbursmentRequest){
        reImbursmentService.save(reImbursmentRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     *
     * @return a list of all Reimbursement
     */
    @GetMapping("/getAll")
    public ResponseEntity getAllReimbursement(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reImbursmentService.getAll());
    }

    /**
     *
     * @param employeeId the id of the employee seeking to get their Reimbursement
     * @param username the user that is login
     * @return a list of all reimbursement by the user
     */

    @GetMapping("/getReimburse/{employeeId}")
    public ResponseEntity getEmployeeReimbursement(@PathVariable int employeeId, @RequestHeader(value = "Cookie") String [] username){
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reimbursement> getAll = reImbursmentService.getReImbursementsById(employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(getAll);
    }


    /**
     *
     * @return the list of employees reimbursement
     */
    @GetMapping("/")
    public ResponseEntity getReimbursementService() {
      List<Reimbursement> getAll = reImbursmentService.getReimbursementByEmployeeId();
        return ResponseEntity.status(HttpStatus.OK).body(getAll);
    }


    /**
     *
     * @return return all Managers
     */
    @PostMapping("/getManagers")
    public ResponseEntity getManages(){

        return ResponseEntity.status(HttpStatus.OK).body(reImbursmentService.getAllManagers());
    }


    /**
     *
     * @param update the Reimbursement to be updated
     * @return a status of OK.
     */
    @PostMapping("/updateReimburse")
    public ResponseEntity updateReimburse(@RequestBody updateReimbursement update){
        reImbursmentService.updateReimbursementById(update);
        return ResponseEntity.status(HttpStatus.OK).build();


    }

    @PostMapping("/getAllProcess")
    public ResponseEntity getProcess(@RequestBody ProcessRequest processRequest){
        return ResponseEntity.status(HttpStatus.OK).body(reImbursmentService.getAllProcess(processRequest.getReimburseId()));
    }



}
