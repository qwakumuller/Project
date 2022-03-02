package com.example.reimbursement.Service;


import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Repository.CompanyRepository;
import com.example.reimbursement.Utils.EmployeeRequest;
import com.example.reimbursement.Utils.Roles;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Base64;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class CompanyTest {

    @Mock
    private CompanyRepository companyRepository;




    @Mock
    private Mailer mailer;


    @InjectMocks
    private CompanyService companyService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("I am here");
    }

    private EmployeeRequest employee(){
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setEmailAddress("one@yahoo.com");
        employeeRequest.setFirstName("First");
        employeeRequest.setLastName("Last");
        employeeRequest.setUserName("user");
        employeeRequest.setPassword("pass");

        return employeeRequest;

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

    @Test()
    public void AddEmployee(){
        Optional<Employee> returnEmployee = Optional.empty();
        Mockito.when(companyRepository.findEmployeeByUserName(anyString())).thenReturn(returnEmployee);
        Mockito.when(mailer.sendMail(any(), any(), anyString())).thenReturn("");

        companyService.saveEmployee(employee());
        ArgumentCaptor<Employee> addEmployee = ArgumentCaptor.forClass(Employee.class);
        verify(companyRepository).save(addEmployee.capture());
        Employee getEmployee = addEmployee.getValue();
        Assert.assertEquals("UserName should Be The Same",getEmployee.getUserName(), "user");
        Assert.assertEquals("Account Should have a role of Employee",getEmployee.getRole(), Roles.EMPLOYEE);

    }


    @Test()
    public void AddManager(){
        Optional<Employee> returnEmployee = Optional.empty();
        Mockito.when(companyRepository.findEmployeeByUserName(anyString())).thenReturn(returnEmployee);
        Mockito.when(mailer.sendMail(any(), any(), any())).thenReturn("");

        companyService.saveManager(employee());
        ArgumentCaptor<Employee> addEmployee = ArgumentCaptor.forClass(Employee.class);
        verify(companyRepository).save(addEmployee.capture());
        Employee getEmployee = addEmployee.getValue();
        Assert.assertEquals("Account Verify Should be False",getEmployee.getIsEmailVerified(), false);
        Assert.assertEquals("Account Should have a role of Manager",getEmployee.getRole(), Roles.MANAGER);

    }


    @Test
   public  void verifyUser() {
        String employeeId = "1009";
        Optional<Employee> employee = Optional.of(newEmployee());
        Mockito.when(companyRepository.findById(anyInt())).thenReturn(employee);
        companyService.verifyUser( Base64.getEncoder().encodeToString(employeeId.getBytes()));
        ArgumentCaptor<Employee> captureEmployee = ArgumentCaptor.forClass(Employee.class);
        verify(companyRepository).save(captureEmployee.capture());


        Assert.assertEquals("User Should Be Verified",captureEmployee.getValue().getIsEmailVerified(), true);
    }
}