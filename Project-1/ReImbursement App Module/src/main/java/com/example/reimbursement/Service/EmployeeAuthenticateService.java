package com.example.reimbursement.Service;

import com.example.reimbursement.Exceptions.VerifyAccount;
import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Model.EmployeeAuthenticate;
import com.example.reimbursement.Repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeAuthenticateService implements UserDetailsService {

    @Autowired
    private CompanyService companyService;

    private static Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee getByUserName = companyService.getEmployeeByUserName(username);
        if(getByUserName == null || getByUserName.getUserName() == null){
            logger.error("User Does Not Exist Upon Login");
            throw new UsernameNotFoundException("User Does Not Exist ");
        }
            logger.info("User Exist while Login");
        return new EmployeeAuthenticate(getByUserName);
    }


}
