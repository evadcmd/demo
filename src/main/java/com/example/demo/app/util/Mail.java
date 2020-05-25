package com.example.demo.app.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "spring.mail")
public class Mail {
    private static final String templateURI = "mail";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Getter
    @Setter
    private String username;

    public void sendText(
            String to,
            String subject,
            String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(message);
    }

    public void send(String to) throws Exception {
        Context context = new Context();
        context.setVariable("username", "m-chiu");
        sendText(to, "Warning", templateEngine.process(templateURI, context));
    }
}