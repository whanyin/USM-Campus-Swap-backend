//package com.cmt322.usmsecondhand.controller;
//
//import com.cmt322.usmsecondhand.common.BaseResponse;
//import com.cmt322.usmsecondhand.common.ErrorCode;
//import com.cmt322.usmsecondhand.common.ResultUtils;
//import com.cmt322.usmsecondhand.service.impl.GmailEmailService;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/test")
//@Slf4j
//public class TestEmailController {
//
//    @Resource
//    private GmailEmailService gmailEmailService;
//
//    @Value("${test.email:wanghanyin@student.usm.my}")
//    private String testEmail;
//
//    /**
//     * Test email connection
//     */
//    @GetMapping("/email/connection")
//    public BaseResponse<String> testConnection() {
//        try {
//            boolean connected = gmailEmailService.testConnection();
//
//            if (connected) {
//                return ResultUtils.success("Gmail SMTP connection test successful!");
//            } else {
//                return ResultUtils.error(ErrorCode.EMAIL_CONNECT_ERROR, "Gmail SMTP connection failed");
//            }
//        } catch (Exception e) {
//            log.error("Connection test exception", e);
//            return ResultUtils.error(ErrorCode.EMAIL_CONNECT_ERROR, "Connection test exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Send test email
//     */
//    @PostMapping("/email/send")
//    public BaseResponse<String> sendTestEmail(
//            @RequestParam(required = false) String email,
//            @RequestParam(defaultValue = "654321") String code) {
//
//        String targetEmail = email != null ? email : testEmail;
//
//        try {
//            log.info("Starting to send test email to: {}", targetEmail);
//
//            boolean success = gmailEmailService.sendVerificationCode(targetEmail, code);
//
//            if (success) {
//                String message = String.format(
//                        "Test email sent successfully!\n\n" +
//                                "Recipient: %s\n" +
//                                "Verification Code: %s\n\n" +
//                                "Please check your inbox (including spam folder).",
//                        targetEmail, code
//                );
//
//                return ResultUtils.success(message);
//            } else {
//                return ResultUtils.error(ErrorCode.EMAIL_SEND_ERROR, "Email sending failed, please check server logs");
//            }
//        } catch (Exception e) {
//            log.error("Exception while sending test email", e);
//            return ResultUtils.error(ErrorCode.EMAIL_SEND_ERROR, "Sending exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Complete verification process test
//     */
//    @PostMapping("/email/full-test")
//    public BaseResponse<String> fullEmailTest() {
//        try {
//            StringBuilder result = new StringBuilder();
//            result.append("Email verification function complete test\n\n");
//
//            // 1. Test connection
//            result.append("1. Testing SMTP connection...\n");
//            boolean connected = gmailEmailService.testConnection();
//            if (connected) {
//                result.append("Connection successful\n");
//            } else {
//                result.append("Connection failed\n");
//                return ResultUtils.error(ErrorCode.EMAIL_CONNECT_ERROR, result.toString());
//            }
//
//            // 2. Send test email
//            result.append("\n2. Sending test email...\n");
//            String testCode = String.valueOf((int)(Math.random() * 900000) + 100000);
//            boolean sent = gmailEmailService.sendVerificationCode(testEmail, testCode);
//
//            if (sent) {
//                result.append("   Email sent successfully\n");
//                result.append("   Recipient: ").append(testEmail).append("\n");
//                result.append("   Verification Code: ").append(testCode).append("\n");
//            } else {
//                result.append("   Email sending failed\n");
//                return ResultUtils.error(ErrorCode.EMAIL_SEND_ERROR, result.toString());
//            }
//
//            result.append("\nAll tests passed! Please check your email.");
//            return ResultUtils.success(result.toString());
//
//        } catch (Exception e) {
//            log.error("Complete test exception", e);
//            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Test exception: " + e.getMessage());
//        }
//    }
//}