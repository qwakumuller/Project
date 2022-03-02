package com.example.emailapi.Controller;

import com.example.emailapi.Model.Employee;
import com.example.emailapi.Model.Reimbursement;
import com.example.emailapi.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    public void sendMail() throws Exception {
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setReimbursementAmount(100);
        reimbursement.setEmployeeId(1020);
        reimbursement.setReimbursementId(111);
      Mockito.doNothing().when(emailService).sendMessage("one", "one", "one");
      mockMvc.perform(MockMvcRequestBuilders.get("/one", reimbursement, "one@mail.com"))
              .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void verifyUser() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId(1002);
        employee.setEmailAddress("devmuller@gmail.com");
        employee.setPassword("password");
        employee.setFirstName("First");
        employee.setLastName("Last");
        employee.setUserName("user");
        Mockito.doNothing().when(emailService).sendMessage(employee.getEmailAddress(), "test", "TEST");
        mockMvc.perform(MockMvcRequestBuilders.post("/verify", employee).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }


}