package com.example.demo.app.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

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

    // JavaMailSenderImpl is thread-safe once it is constructed.
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // sender info
    // read-only: thread-safe
    @Getter
    @Setter
    private String username;

    public void sendText(
            String to,
            String subject,
            String text,
            byte[] img) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(message, true);
        msgHelper.setFrom(username);
        msgHelper.setTo(to);
        msgHelper.setSubject(subject);
        msgHelper.setText(text, true);
        if (img != null)
            msgHelper.addAttachment("test.jpeg", new ByteArrayDataSource(img, "image/jpeg"));
        mailSender.send(message);
    }

    public void sendText(
            String to,
            String subject,
            String text) throws MessagingException {
        sendText(to, subject, text, null);
    }

    public void send(String to) throws Exception {
        Context context = new Context();
        context.setVariable("username", "m-chiu");
        sendText(to, "Warning", templateEngine.process(templateURI, context));
    }
}