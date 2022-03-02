package com.example.reimbursement.Service;

import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Model.Reimbursement;
import com.example.reimbursement.Repository.ReimbursementProcessRepository;
import com.example.reimbursement.Repository.ReimbursmentRepository;
import com.example.reimbursement.Utils.ReimbursmentRequest;
import com.example.reimbursement.Utils.Status;
import com.example.reimbursement.Utils.updateReimbursement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReimbursementServiceTest {


    @Mock
    private ReimbursmentRepository reImbursmentRepository;


    @Mock
    private CompanyService companyService;

    @Mock
    HttpServletRequest request;

    @Mock
    private Mailer mailer;

    @Mock
    ReimbursementProcessRepository reimbursementProcessRepository;

    @InjectMocks
    private ReImbursmentService reImbursmentService;




    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("I am here");

    }


    private Employee newEmployee(){
        Employee employee = new Employee();
        employee.setEmployeeId(123);
        employee.setEmailAddress("email@yahoo.com");
        employee.setIsEmailVerified(false);
        employee.setUserName("user");
        employee.setFirstName("First");
        employee.setLastName("Last");
        employee.setPassword("pass");
        return employee;
    }


    private Reimbursement newReimbursement(){
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setStatus(Status.UNDER_REVIEW);
        reimbursement.setPurpose("Test");
        reimbursement.setReimbursementId(001);
        reimbursement.setAssignedManager("Test,Manager");

        return reimbursement;
    }



    @Test
   public void saveTest() {

        ReimbursmentRequest reImbursmentRequest = new ReimbursmentRequest(200, "Test");
        Mockito.when(request.getHeader(anyString())).thenReturn("one");
        Mockito.when(companyService.getEmployeeByUserName(anyString())).thenReturn(newEmployee());
        Mockito.when(mailer.createReimbursementSendMail(anyString(), any(), anyString())).thenReturn("");
        reImbursmentService.save(reImbursmentRequest);

        ArgumentCaptor<Reimbursement> captureReimbursement = ArgumentCaptor.forClass(Reimbursement.class);
        verify(reImbursmentRepository).save(captureReimbursement.capture());

        Assert.assertEquals("Reimbursement Return the Wrong Purpose",captureReimbursement.getValue().getPurpose(), "Test");
        Assert.assertEquals("Initial Status should be UnderReview", captureReimbursement.getValue().getStatus(), Status.UNDER_REVIEW);
        Assert.assertEquals("Reimbursement Amount has Changed!",captureReimbursement.getValue().getReimbursementAmount(), 200);


    }


    @Test
    void assignManager() {
        Employee employee1 = newEmployee();
        employee1.setFirstName("Employee2");
        employee1.setLastName("Test2");
        List<Employee> allManagers = Arrays.asList(newEmployee(), employee1);
        Mockito.when(companyService.getManagers()).thenReturn(allManagers);
        String manager = reImbursmentService.assignManager();

        Assert.assertTrue("Manager should not be empty", manager.length() > 0);

    }


    @Test
    void assignManagerWhenManagerIsNull() {
        Employee employee1 = newEmployee();
        employee1.setFirstName("Employee2");
        employee1.setLastName("Test2");
        List<Employee> allManagers = new ArrayList<>();
        Mockito.when(companyService.getManagers()).thenReturn(allManagers);
        String manager = reImbursmentService.assignManager();

        Assert.assertTrue("Manager should be empty", manager.length() == 0);

    }



    @Test
    void updateReimbursementById() {
        updateReimbursement update = new updateReimbursement(001, "APPROVED", "Change,Manager");
        Optional<Reimbursement> getReimbursement = Optional.of(newReimbursement());
        Mockito.when(mailer.updateReimbursementSendMail(anyString(), any(), anyString())).thenReturn("");
        Mockito.when(reImbursmentRepository.findReimbursementByReimbursementId(update.getReimburseId())).thenReturn(getReimbursement);
        Mockito.when(companyService.getEmployeeByUserName(anyString())).thenReturn(newEmployee());
        Mockito.when(request.getHeader("User")).thenReturn("user");
        reImbursmentService.updateReimbursementById(update);

        ArgumentCaptor<Reimbursement> captureReimbursement = ArgumentCaptor.forClass(Reimbursement.class);
        verify(reImbursmentRepository).save(captureReimbursement.capture());
        Reimbursement reimbursement = captureReimbursement.getValue();

        Assert.assertEquals("Reimbursement Id should be the same", reimbursement.getReimbursementId(), newReimbursement().getReimbursementId());
        Assert.assertEquals("Amount should be the same",reimbursement.getReimbursementAmount(), newReimbursement().getReimbursementAmount());
        Assert.assertEquals("Status should Change",reimbursement.getStatus(), Status.APPROVED);
        Assert.assertEquals("Purpose should Remain the same",reimbursement.getPurpose(), newReimbursement().getPurpose());
        Assert.assertEquals("Manager name should change", reimbursement.getAssignedManager(),"Change,Manager" );

    }


    @Test
    void updateReimbursementByIdWithOnlyManager() {
        updateReimbursement update = new updateReimbursement(001, "UNDER_REVIEW", "Change,Manager");
        Optional<Reimbursement> getReimbursement = Optional.of(newReimbursement());
        Mockito.when(mailer.updateReimbursementSendMail(anyString(), any(), anyString())).thenReturn("");
        Mockito.when(companyService.getEmployeeByUserName(anyString())).thenReturn(newEmployee());
        Mockito.when(reImbursmentRepository.findReimbursementByReimbursementId(update.getReimburseId())).thenReturn(getReimbursement);
        Mockito.when(request.getHeader("User")).thenReturn("user");
        reImbursmentService.updateReimbursementById(update);

        ArgumentCaptor<Reimbursement> captureReimbursement = ArgumentCaptor.forClass(Reimbursement.class);
        verify(reImbursmentRepository).save(captureReimbursement.capture());
        Reimbursement reimbursement = captureReimbursement.getValue();

        Assert.assertEquals("Reimbursement Id should be the same", reimbursement.getReimbursementId(), newReimbursement().getReimbursementId());
        Assert.assertEquals("Amount should be the same",reimbursement.getReimbursementAmount(), newReimbursement().getReimbursementAmount());
        Assert.assertEquals("Status should not  Change",reimbursement.getStatus(), Status.UNDER_REVIEW);
        Assert.assertEquals("Purpose should Remain the same",reimbursement.getPurpose(), newReimbursement().getPurpose());
        Assert.assertEquals("Manager name should change", reimbursement.getAssignedManager(),"Change,Manager" );

    }
}