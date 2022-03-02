package com.example.reimbursement.Security;


import com.example.reimbursement.Exceptions.JWTExpired;
import com.example.reimbursement.Service.EmployeeAuthenticateService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private EmployeeAuthenticateService employeeAuthenticateService;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;


        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(7);
            logger.info("Token is present" + token);
            try {
                username = jwtUtils.getUsernameFromToken(token);
                System.out.println(username);
                Cookie cookie = new Cookie("username", username);
                response.addCookie(cookie);
            } catch (IllegalArgumentException ae) {
                logger.fatal("Unable to get token");
            } catch (ExpiredJwtException ee) {
            logger.fatal("Token has Expired");
             //throw new JWTExpired();

            }
        } else {
            logger.warn("JWT does not begin with Bearer string");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = employeeAuthenticateService.loadUserByUsername(username);

            if(jwtUtils.validate(token, userDetails)) {
                logger.info("Token valid");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null ? Collections.emptyList() : userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
