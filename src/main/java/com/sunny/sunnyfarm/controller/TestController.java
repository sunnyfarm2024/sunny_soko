package com.sunny.sunnyfarm.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/google")
public class TestController {
    @PostMapping("/email")
    public String receiveEmail(@RequestBody String email) {
        System.out.println("Received email: " + email);
        return "Email: " + email + " 연동 성공.";
    }

    public static class EmailRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
