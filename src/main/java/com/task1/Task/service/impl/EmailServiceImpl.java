package com.task1.Task.service.impl;

import com.task1.Task.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String content) {
//            System.out.println("===================================");
//            System.out.println("📩 Email Notification 📩");
//            System.out.println("To: " + to);
//            System.out.println("Subject: " + subject);
//            System.out.println("Message: " + content);
//            System.out.println("===================================");

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("adityakukade8@gmail.com");
        message.setTo(to);
        message.setText(content);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail Sent Successfully....");


    }
}
