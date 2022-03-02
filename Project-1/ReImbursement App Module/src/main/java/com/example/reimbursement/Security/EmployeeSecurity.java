package com.example.reimbursement.Security;

import com.example.reimbursement.Service.EmployeeAuthenticateService;
import com.example.reimbursement.Utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class EmployeeSecurity extends WebSecurityConfigurerAdapter {


    @Autowired
    private EmployeeAuthenticateService employeeAuthenticateService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(employeeAuthenticateService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.cors()
               .and()
               .csrf()
               .disable()
               .authorizeRequests()
               .antMatchers(HttpMethod.POST,"/auth/**").permitAll()
               .antMatchers("/api/getAll").hasAuthority(Roles.MANAGER.name())
               .antMatchers("/employee/createEmployee").permitAll()
               .antMatchers("/employee/createManager").permitAll()
               .antMatchers("/employee/verifyEmail/**").permitAll()
               .antMatchers("/actuator/**").permitAll()
               .anyRequest().authenticated();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
