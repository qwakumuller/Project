package com.example.emailapi.Controller;

import com.example.emailapi.Model.Employee;
import com.example.emailapi.Model.Reimbursement;
import com.example.emailapi.Model.UpdateMailRequest;
import com.example.emailapi.Service.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import java.util.Base64;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Value("${reimbursement-config-URL}")
    String reimbursementUrl;

    private static Logger logger = LoggerFactory.getLogger(EmailController.class);



    @PostMapping("/one")
    public ResponseEntity sendMail(@RequestBody UpdateMailRequest reimbursement){
        try{
            emailService.sendMessage("richmondawuahadjei@yahoo.com",createMailMessage(reimbursement)  ,"REIMBURSEMENT SUCCESSFULLY CREATED");
            emailService.sendMessage(reimbursement.getEmail(), createMailMessage(reimbursement), "REIMBURSEMENT SUCCESSFULLY CREATED" );
        } catch (SendFailedException e) {
            logger.error("Error while sending mail " + e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            logger.error("Error while sending mail " + e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();

    }


    @PostMapping("/verify")
    public ResponseEntity verifyUser(@RequestBody Employee employee){
        try{
            emailService.sendMessage("richmondawuahadjei@yahoo.com",verifyAccountMessage(employee)  ,"VERIFY ACCOUNT");
            emailService.sendMessage(employee.getEmailAddress(), verifyAccountMessage(employee)  ,"VERIFY ACCOUNT");
        } catch (SendFailedException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }



    @PostMapping("/update")
    public void updateMail(@RequestBody UpdateMailRequest reImbursement){
        try{
            emailService.sendMessage("richmondawuahadjei@yahoo.com",updateMailMessage(reImbursement)  ,"REIMBURSEMENT HAS BEEN UPDATED");
            emailService.sendMessage(reImbursement.getEmail(),updateMailMessage(reImbursement)  ,"REIMBURSEMENT HAS BEEN UPDATED");
        } catch (SendFailedException e) {
            logger.error("Error while sending mail " + e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            logger.error("Error while sending mail " + e.getMessage());
            e.printStackTrace();
        }

    }


    private String createMailMessage(UpdateMailRequest reImbursement){
        return " Hey, " +
                "your ReimbursementId is " + reImbursement.getReimbursementId() +
                " for an amount of " + reImbursement.getReimbursementAmount()
                + " and your status is " + reImbursement.getStatus();
    }

    private String updateMailMessage(UpdateMailRequest reImbursement){
        return " Hey, " +
                "your ReimbursementId is " + reImbursement.getReimbursementId() +
                " for an amount of " + reImbursement.getReimbursementAmount()
                + " has been updated to a status " + reImbursement.getStatus();
    }

    private String verifyAccountMessage(Employee employee){
        String employeeId = Integer.toString(employee.getEmployeeId());
        String encryptEmployeeId = Base64.getEncoder().encodeToString(employeeId.getBytes());
        String addToUrl = RandomStringUtils.randomAlphanumeric(22);

        return "Please Verify your Email Address \n" +
                reimbursementUrl+"/verifyEmail/"+encryptEmployeeId+"/"+addToUrl;
    }



}
