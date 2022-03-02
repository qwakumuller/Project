package com.example.reimbursement.Service;

import com.example.reimbursement.Exceptions.ServerDown;
import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Model.Reimbursement;
import com.example.reimbursement.Utils.UpdateMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Mailer {

    @Autowired
    private WebClient.Builder webClient;

    private static Logger logger = LoggerFactory.getLogger(Mailer.class);

    /**
     *
     * @param url the url of the EmailAPI
     * @param newEmployee the new employee that is been added
     * @return an empty string
     */
    public String sendMail(String url, Employee newEmployee,String email){

        webClient.build()
                .post()
                .uri(url)
                .body(Mono.just(newEmployee), Employee.class)
                .retrieve()
                .bodyToMono(Employee.class)
                .doOnError(error -> logger.error("Error occurred when Verifying email " + error.getMessage()))
                .block();
        return"";

    }


    /**
     *
     * @param url the url of the EmailAPI
     * @param reimbursement the reimbursement that is created by the employee
     * @return an empty String
     */
    public String createReimbursementSendMail(String url, Reimbursement reimbursement, String email){
        UpdateMailRequest updateMailRequest = new UpdateMailRequest();
        updateMailRequest.setReimbursementId(reimbursement.getReimbursementId());
        updateMailRequest.setEmail(email);
        updateMailRequest.setStatus(reimbursement.getStatus());
        updateMailRequest.setReimbursementAmount(reimbursement.getReimbursementAmount());

        webClient.build()
                .post()
                .uri(url)
                .body(Mono.just(updateMailRequest), UpdateMailRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, error -> {
                   logger.error("Other Server is Down " + error);
                    Mono.error(new ServerDown());
                    return null;
                })
                .bodyToMono(Reimbursement.class)
                .block();
        return"";

    }


    /**
     *
     * @param url the url of the EmailAPI
     * @param reimbursement the reimbursement that is updated
     * @return an empty String
     */
    public String updateReimbursementSendMail(String url, Reimbursement reimbursement, String email){
        UpdateMailRequest updateMailRequest = new UpdateMailRequest();
        updateMailRequest.setReimbursementId(reimbursement.getReimbursementId());
        updateMailRequest.setEmail(email);
        updateMailRequest.setStatus(reimbursement.getStatus());
        updateMailRequest.setReimbursementAmount(reimbursement.getReimbursementAmount());

        webClient.build()
                .post()
                .uri(url)
                .body(Mono.just(updateMailRequest),UpdateMailRequest.class )
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, error -> {
                    logger.error("Other Server is Down " + error);
                    Mono.error(new ServerDown());
                    return null;
                })
                .bodyToMono(Reimbursement.class)
                .block();
        return"";

    }
}
