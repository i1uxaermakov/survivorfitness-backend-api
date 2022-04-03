package com.changeplusplus.survivorfitness.backendapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String emailTo, String subject, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailTo);
        msg.setSubject(subject);
        msg.setText(message);

        try {
            javaMailSender.send(msg);
        }
        catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An Exception occurred while sending an email to " + Arrays.toString(msg.getTo()) + "\nException details: " + e);
        }
    }


    /**
     *
     * @param emailTo
     * @param subject
     * @param messageText
     * @param attachment
     */
    public void sendEmailWithAttachment(String emailTo, String subject, String messageText, File attachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(messageText);
            helper.addAttachment(attachment.getName(), attachment);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An Exception occurred while sending an email to " + (emailTo) + "\nException details: " + e);
        }
    }
}
