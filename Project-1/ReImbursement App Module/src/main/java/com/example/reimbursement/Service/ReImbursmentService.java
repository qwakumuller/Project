package com.example.reimbursement.Service;

import com.example.reimbursement.Model.Employee;
import com.example.reimbursement.Model.Reimbursement;
import com.example.reimbursement.Model.ReimbursementProcess;
import com.example.reimbursement.Repository.ReimbursementProcessRepository;
import com.example.reimbursement.Repository.ReimbursmentRepository;
import com.example.reimbursement.Utils.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ReImbursmentService {

    @Autowired
    private ReimbursmentRepository reImbursmentRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Mailer mailer;

    @Autowired
    private CompanyService companyService;


    @Autowired
    private ReimbursementProcessRepository reimbursementProcessRepository;


    @Value("${email.config.url}")
    String emailUrl;

    private static Logger logger = LoggerFactory.getLogger(ReImbursmentService.class);


    /**
     *
     * @param reImbursmentRequest the request made by the employee
     * @return a Reimbursement then save into out database
     */
    public Reimbursement save(ReimbursmentRequest reImbursmentRequest){
        int generateID = Integer.parseInt(RandomStringUtils.randomNumeric(8));
        Reimbursement reImbursement = new Reimbursement();
        reImbursement.setReimbursementAmount(reImbursmentRequest.getReimbursementAmount());
        reImbursement.setReimbursementId(generateID);
        reImbursement.setStatus(Status.UNDER_REVIEW);
        reImbursement.setPurpose(reImbursmentRequest.getPurpose());
        reImbursement.setReimbursementCreatedDate(new Date());
        String getLoginUser = request.getHeader("User");
        logger.debug("User is signed in as" + getLoginUser);

        Employee employee = companyService.getEmployeeByUserName(getLoginUser);
        reImbursement.setEmployeeId(employee.getEmployeeId());
        reImbursement.setAssignedManager(assignManager());
        logger.info("Reimbursement has been assigned to a manager");

        String url = emailUrl+"/one";
        logger.info("Sending mail");
        mailer.createReimbursementSendMail(url, reImbursement, employee.getEmailAddress());

        return reImbursmentRepository.save(reImbursement);
    }

    /**
     *
     * @return all reimbursement in our Database
     */
    public List<Reimbursement> getAll(){
        return reImbursmentRepository.findAll();
    }


    /**
     * Chooses a manager at random
     * @return String of firstName and lastName
     */
    public String assignManager(){
        String assignedManager ="";
        List<Employee> getAllManager = companyService.getManagers();
        if(getAllManager.size() > 0){
            int randomManager = new Random().nextInt(getAllManager.size());
            Employee employee = getAllManager.get(randomManager);
            assignedManager = new String(employee.getFirstName()+","+employee.getLastName());
            logger.debug("A manager was assigned successfully");
        }
        return  assignedManager;
    }


    /**
     *
     * @param employeeId the Id of the employee
     * @return a list of all Reimbursement by the employee
     */
    public List<Reimbursement> getReImbursementsById(int employeeId){
        return reImbursmentRepository.findReimbursementByEmployeeId(employeeId);
    }


    /**
     *
     * @return a list of all employees Reimbursement
     */
    public List<Reimbursement> getReimbursementByEmployeeId(){
        String getLoginUser = request.getHeader("User");
        Employee employee = companyService.getEmployeeByUserName(getLoginUser);
        return reImbursmentRepository.findReimbursementByEmployeeId(employee.getEmployeeId());
    }


    /**
     *
     * @return a list of all Managers in our Database
     */
    public List<managerResponse> getAllManagers(){
        List<Employee> allmanagers  = companyService.getManagers();
       return allmanagers.stream()
                .map(manager -> {
                    String fullName = manager.getFirstName()+","+manager.getLastName();
                    managerResponse response = new managerResponse(manager.getEmployeeId(),fullName );
                    return response;
                }).collect(Collectors.toList());
    }


    public void updateReimbursementById(updateReimbursement update){
        Optional<Reimbursement> reimbursements= reImbursmentRepository.findReimbursementByReimbursementId(update.getReimburseId());
        if(reimbursements.isPresent()){
            Reimbursement reimbursement = reimbursements.get();
            reimbursement.setStatus(Status.valueOf(update.getStatus()));
            reimbursement.setAssignedManager(update.getAssign());
            String getLoginUser = request.getHeader("User");
            Employee employee = companyService.getEmployeeByUserName(getLoginUser);
            String email = employee.getEmailAddress();

            String url = emailUrl+"/update";
            logger.debug("Sending Update Reimbursement Mail");
           mailer.updateReimbursementSendMail(url, reimbursement, email);

            reimbursementProcessRepository.save(create(reimbursement));
            reImbursmentRepository.save(reimbursement);
        }



    }

    public List<ReimbursementProcess> getAllProcess (int reimburseId){
        return reimbursementProcessRepository.findReimbursementProcessByReimbursementId(reimburseId);

    }

    private ReimbursementProcess create(Reimbursement reimbursement){
        ReimbursementProcess reimbursementProcess = new ReimbursementProcess();
        reimbursementProcess.setReimbursementId(reimbursement.getReimbursementId());
        reimbursementProcess.setReimbursementAmount(reimbursement.getReimbursementAmount());
        reimbursementProcess.setEmployeeId(reimbursement.getEmployeeId());
        reimbursementProcess.setPurpose(reimbursement.getPurpose());
        reimbursementProcess.setAssignedManager(reimbursement.getAssignedManager());
        reimbursementProcess.setStatus(reimbursement.getStatus());
        reimbursementProcess.setReimbursementCreatedDate(new Date());

        return reimbursementProcess;
    }


}
