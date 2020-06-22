package com.example.demo.app.util;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import com.example.demo.app.entity.DetectionLog;
import com.example.demo.auth.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@ConfigurationProperties(prefix = "spring.mail")
public class Mail {
    private static final String templateURI = "mail";

    @Autowired
    private IP ip;

    // JavaMailSenderImpl is thread-safe once it is constructed.
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // sender info
    // read-only: thread-safe
    @Value("${spring.mail.username}")
    private String username;

    /*
    @Value("${xware-videoserver.url}")
    private String videoURL;
    */

    public void send(
            String to,
            String subject,
            String text,
            String jpegName,
            byte[] img) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(message, true);
        msgHelper.setFrom(username);
        msgHelper.setTo(to);
        msgHelper.setSubject(subject);
        msgHelper.setText(text, true);
        if (img != null)
            msgHelper.addAttachment(jpegName, new ByteArrayDataSource(img, "image/jpeg"));
        mailSender.send(message);
    }

    public void send(
            String to,
            String subject,
            String text) throws MessagingException {
        send(to, subject, text, null, null);
    }

    public void send(User user, DetectionLog log) throws Exception {
        Context context = new Context();
        String ipStr = IP.valueOf(log.getCameraId());
        String timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getAt());
        context.setVariables(
            Map.of(
                "url", ip.getSelfURL(),
                "username", user.getUsername(),
                "at", timeFmt,
                "cameraLabel", log.getCameraLabel(),
                "cameraIp", ipStr,
                "numHuman", log.getNumHuman(),
                "maxConfidence", log.getMaxConfidence()
                // "xwareVideoURL", videoURL
            )
        );
        send(
            user.getEmail(),
            "監視カメラ人検知情報",
            templateEngine.process(templateURI, context),
            String.format("%s_%s.jpeg", ipStr, timeFmt),
            log.getImg()
        );
    }
}