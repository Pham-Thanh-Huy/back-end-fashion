package com.example.fashion.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
public class EmailConfig{

    private JavaMailSender javaMailSender;

    public void sendMail(String from, String to, String subject, String text){
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMailMessage);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
