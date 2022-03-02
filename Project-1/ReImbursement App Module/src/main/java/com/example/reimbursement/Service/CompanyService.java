package com.example.reimbursement.Service;

import com.example.reimbursement.Exceptions.UserExist;
import com.example.reimbursement.Exceptions.VerifyAccount;
import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Model.EmployeeAuthenticate;
import com.example.reimbursement.Repository.CompanyRepository;
import com.example.reimbursement.Utils.EmployeeRequest;
import com.example.reimbursement.Utils.EmployeeResponse;
import com.example.reimbursement.Utils.Roles;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private Mailer mailer;

    @Value("${email.config.url:http://localhost:8082}")
    String verifyEmail;

    private static Logger logger = LoggerFactory.getLogger(CompanyService.class);





    /**
     *
     * @param employee the employee to be created
     * @return the created employee
     */
    public Employee saveEmployee(EmployeeRequest employee){
        int generateID = Integer.parseInt(RandomStringUtils.randomNumeric(8));
        boolean userNameExist = getEmployeeByUserName(employee.getUserName()) == null ? false : true;
        if(userNameExist == true){
            MDC.put("events", "UserExist");
            logger.error("User Already Exist In The Database");
            MDC.clear();
            throw new UserExist();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        Employee newEmployee = new Employee(employee.getFirstName(), employee.getLastName(), employee.getUserName(), employee.getEmailAddress(), encodedPassword);
        newEmployee.setEmployeeId(generateID);
        newEmployee.setIsEmailVerified(false);
        newEmployee.setRole(Roles.EMPLOYEE);

        companyRepository.save(newEmployee);

        String url = verifyEmail+"/verify";
        logger.info("Sending Mail");
        mailer.sendMail(url, newEmployee, employee.getEmailAddress());

        return newEmployee;

    }



    /**
     *
     * @param employee the employee to be created
     * @return the created Manager
     */
    public Employee saveManager(EmployeeRequest employee) throws UserExist{
        int generateID = Integer.parseInt(RandomStringUtils.randomNumeric(8));
        boolean userNameExist = getEmployeeByUserName(employee.getUserName()) == null ? false : true;
        if(userNameExist == true){
            MDC.put("Exception", "UserExist");
            logger.error("User Already Exist In The Database");
            MDC.clear();
            throw new UserExist();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        Employee newEmployee = new Employee(employee.getFirstName(), employee.getLastName(), employee.getUserName(), employee.getEmailAddress(), encodedPassword);
        newEmployee.setEmployeeId(generateID);
        newEmployee.setRole(Roles.MANAGER);
        newEmployee.setIsEmailVerified(false);

        String url = verifyEmail+"/verify";
        mailer.sendMail(url, newEmployee, employee.getEmailAddress());

        return companyRepository.save(newEmployee);
    }


    /**
     *
     * @return the list of all users in the Database
     */
    public List<Employee> getAll(){
        return companyRepository.findAll();
    }


    /**
     *
     * @param employeeId the employee id
     * @return the employee with the employeeId specified
     */
    public Employee getEmployeeById(int employeeId){
        Optional<Employee> getEmployee = companyRepository.findById(employeeId);
        return getEmployee.isPresent() ? getEmployee.get() : null;
    }

    /**
     *
     * @param roles the role employees have
     * @return the list of employee with Such role
     */
    public List<Employee> getEmployeeByRoles(Roles roles){
        return companyRepository.findEmployeeByRole(roles);
    }

    /**
     *
     * @param username the username that exist in the database
     * @return the employee with such username from the database
     */
    public Employee getEmployeeByUserName(String username){
       Optional<Employee> getEmployeeByUserName =  companyRepository.findEmployeeByUserName(username);
        return getEmployeeByUserName.isPresent() ? getEmployeeByUserName.get() : null;
    }

    public boolean verifyUser(String urlEmployeeId) {
        boolean verifyEmployee = false;
        String getEmployeeId = new String(Base64.getDecoder().decode(urlEmployeeId));
        int employeeId = Integer.parseInt(getEmployeeId);
        Optional<Employee> employee = companyRepository.findById(employeeId);
        if(employee.isPresent()){
            logger.info("Employee is Present and Verified");
            employee.get().setIsEmailVerified(true);
            verifyEmployee = true;
            companyRepository.save(employee.get());
        }

        return verifyEmployee;
    }

    public boolean isUserVerified(String username){
        Employee getByUserName = getEmployeeByUserName(username);
        if(getByUserName == null || getByUserName.getUserName() == null){
            MDC.put("events", "UsernameNotFoundException");
            logger.error("User Does Not Exit");
            MDC.clear();
            throw new UsernameNotFoundException("User Does Not Exist ");
        }

        if(getByUserName.getIsEmailVerified() == false){
            MDC.put("events", "VerifyAccount");
            logger.error("Verify your Account via the Email Sent");
            MDC.clear();
            throw new VerifyAccount();
        }

        return true;
    }


    public List<Employee> getManagers(){
        return companyRepository.findEmployeeByRole(Roles.MANAGER);
    }



}
