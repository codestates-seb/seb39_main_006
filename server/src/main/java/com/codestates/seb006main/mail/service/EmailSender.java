package com.codestates.seb006main.mail.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@Service
public class EmailSender {
    private final JavaMailSender mailSender;
    private final SimpleEmailSendable emailSendable;

    public EmailSender(JavaMailSender mailSender, SimpleEmailSendable emailSendable) {
        this.mailSender = mailSender;
        this.emailSendable = emailSendable;
    }

    private String subject = "[이메일 인증] 홈페이지에 인증번호를 입력해주세요.";

    public void sendEmail(String to, String code) throws GeneralSecurityException, UnsupportedEncodingException {
        try {
            String context = "인증번호는 ["+code+"]입니다.";
            emailSendable.send(to, subject, context);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다");
        }
    }
}
