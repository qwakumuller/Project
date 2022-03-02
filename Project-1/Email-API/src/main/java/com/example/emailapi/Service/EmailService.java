package com.example.emailapi.Service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {



private static EmailStatic constant;


    /**
     *
     * @return the session to send the mail
     */
    private Session emailSession(){
        Properties properties = System.getProperties();

        properties.put(EmailStatic.SMTP_HOST, EmailStatic.GMAIL_SMTP_SERVER);
        properties.put(EmailStatic.SMTP_AUTH, true);
        properties.put(EmailStatic.SMTP_PORT, EmailStatic.DEFAULTS_PORT);
        properties.put(EmailStatic.SMTP_START_ENABLE, true);
        properties.put(EmailStatic.SMTP_START_REQUIRED, true);
        return  Session.getInstance(properties);
    }


    /**
     *
     * @param email the email of the employee that need to be sent
     * @param sendMessage the message to be sent to the email user
     * @param subject the subject of the mail
     * @return a messsage
     * @throws AddressException the address is invalid
     * @throws MessagingException the message was not created
     */
    private Message createEmailMessage(String email, String sendMessage, String subject) throws AddressException, MessagingException{
        Message message = new MimeMessage(emailSession());
        message.setFrom(new InternetAddress(EmailStatic.FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(EmailStatic.CC_EMAIL, false));
        message.setSubject(subject);
        message.setText(sendMessage);
        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }


    /**
     *
     * @param email the email to sent the message
     * @param sendMessage the message to be sent
     * @param subject the subject of the email
     * @throws SendFailedException
     * @throws MessagingException
     */
    public void sendMessage(String email, String sendMessage, String subject) throws SendFailedException, MessagingException{
        Message message = createEmailMessage(email, sendMessage, subject);
        SMTPTransport smtpTransport = (SMTPTransport) emailSession().getTransport(EmailStatic.SecureMessageProtocol);
        smtpTransport.connect(EmailStatic.GMAIL_SMTP_SERVER, EmailStatic.EMAIL, EmailStatic.PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

}
