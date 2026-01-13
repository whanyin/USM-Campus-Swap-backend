//package com.cmt322.usmsecondhand.service.impl;
//
//import jakarta.annotation.Resource;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//import java.util.Date;
//import java.util.Random;
//
//@Service
//@Slf4j
//public class GmailEmailService {
//
//    @Resource
//    private JavaMailSender mailSender;
//
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//
//    @Value("${spring.application.name:USM Secondhand}")
//    private String appName;
//
//    public boolean sendVerificationCode(String toEmail, String verificationCode) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom(new InternetAddress(fromEmail, appName));
//            helper.setTo(toEmail);
//            helper.setSubject("USM Secondhand - Email Verification Code");
//
//            // 只发送纯文本
//            String textContent = buildSimpleHtmlContent(verificationCode);
//            helper.setText(textContent);
//
//            mailSender.send(message);
//
//            log.info("Verification email sent to: {}", toEmail);
//            return true;
//
//        } catch (Exception e) {
//            log.error("Failed to send email to {}", toEmail, e);
//            return false;
//        }
//    }
//
//    /**
//     * 构建简单的HTML内容
//     */
//    private String buildSimpleHtmlContent(String code) {
//        return "<html>" +
//                "<body>" +
//                "<h2>USM Secondhand Email Verification</h2>" +
//                "<p>Your verification code is:</p>" +
//                "<h3 style='color: #4285F4; font-size: 24px;'>" + code + "</h3>" +
//                "<p>This code will expire in 5 minutes.</p>" +
//                "<p>If you didn't request this, please ignore this email.</p>" +
//                "</body>" +
//                "</html>";
//    }
//
//    /**
//     * 测试Gmail连接
//     */
//    public boolean testConnection() {
//        try {
//            log.info("Testing Gmail SMTP connection...");
//
//            // 尝试发送测试邮件到自己的邮箱
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message);
//
//            helper.setFrom(fromEmail);
//            helper.setTo(fromEmail); // 发送给自己
//            helper.setSubject("✅ Gmail SMTP Test - " + new Date());
//            helper.setText("Gmail SMTP connection is working properly!");
//
//            mailSender.send(message);
//
//            log.info("✅ Gmail SMTP test successful!");
//            return true;
//        } catch (Exception e) {
//            log.error("❌ Gmail SMTP test failed: {}", e.getMessage());
//
//            // 提供诊断信息
//            if (e.getMessage().contains("535")) {
//                log.error("""
//
//                         ⚠️  COMMON GMAIL ISSUES:
//
//                         1. TWO-FACTOR AUTHENTICATION REQUIRED
//                            • Enable 2FA: https://myaccount.google.com/security
//                            • Generate app password: https://myaccount.google.com/apppasswords
//
//                         2. APP PASSWORD VS LOGIN PASSWORD
//                            • ❌ Don't use your Gmail login password
//                            • ✅ Use 16-character app password
//
//                         3. "LESS SECURE APPS" SETTING (if not using 2FA)
//                            • Try enabling: https://myaccount.google.com/lesssecureapps
//
//                         Current config:
//                         • Host: smtp.gmail.com
//                         • Port: 587
//                         • Username: {}
//                         • Password length: {}
//                         """,
//                        fromEmail,
//                        getPasswordLength());
//            }
//
//            return false;
//        }
//    }
//
//    private int getPasswordLength() {
//        String password = System.getenv("GMAIL_APP_PASSWORD");
//        return password != null ? password.length() : 0;
//    }
//}