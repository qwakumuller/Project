//package com.example.reimbursement.Controller;
//
//import com.example.reimbursement.Model.Employee;
//import com.example.reimbursement.Model.EmployeeAuthenticate;
//import com.example.reimbursement.Security.JwtRequestFilter;
//import com.example.reimbursement.Service.EmployeeAuthenticateService;
//import com.example.reimbursement.Service.ReImbursmentService;
//import com.example.reimbursement.Utils.ReimbursmentRequest;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@WebMvcTest(value = ReImbursementController.class)
//@EnableAutoConfiguration(exclude= {SecurityAutoConfiguration.class,  ManagementWebSecurityAutoConfiguration.class })
//public class ReImbursementControllerTest {
//
//    @MockBean
//   private  ReImbursmentService reImbursmentService;
//
//    @MockBean
//    JwtRequestFilter jwtRequestFilter;
//
//    @Autowired
//   private MockMvc mockMvc;
//
//
//    @Test
//    void createReimbursement() throws Exception {
//
//        ReimbursmentRequest reimbursmentRequest = new ReimbursmentRequest();
//        reimbursmentRequest.setReimbursementAmount(100);
//        reimbursmentRequest.setPurpose("Test");
//        Mockito.when(reImbursmentService.save(reimbursmentRequest)).thenReturn(null);
//        mockMvc.perform(MockMvcRequestBuilders.post("/reimburse", reimbursmentRequest))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//
//    }
//
//    @Test
//    void getReImbursmentService() {
//    }
//
//    @Test
//    void updateReimburse() {
//    }
//}