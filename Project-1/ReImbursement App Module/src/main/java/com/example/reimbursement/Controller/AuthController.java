package com.example.reimbursement.Controller;


import com.example.reimbursement.Security.JWTUtils;
import com.example.reimbursement.Service.CompanyService;
import com.example.reimbursement.Service.EmployeeAuthenticateService;
import com.example.reimbursement.Utils.LoginRequest;
import com.example.reimbursement.Utils.LoginResponse;
import org.apache.tomcat.websocket.WsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin(origins = "http://localhost:3030")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private CompanyService companyService;


    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> generateToken(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        final boolean verifyEmail = companyService.isUserVerified(loginRequest.getUsername());
        final String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new LoginResponse(false, token, userDetails.getUsername(), userDetails.getAuthorities().stream().findFirst().get().toString()));
    }



    private void authenticate(@NonNull String username, @NonNull String password) throws Exception {
     logger.info("Authenticating User");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException de) {
            logger.error("User not active");
        } catch (BadCredentialsException be) {
            logger.error("Invalid Credential " + be);
            throw new Exception("Invalid credentials", be);
        }
    }

}
