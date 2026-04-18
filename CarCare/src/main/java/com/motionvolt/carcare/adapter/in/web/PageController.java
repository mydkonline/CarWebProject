package com.motionvolt.carcare.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/main"})
    public String main() {
        return "main";
    }

    @GetMapping("/service")
    public String service() {
        return "service";
    }

    @GetMapping("/brand")
    public String brand() {
        return "brand";
    }

    @GetMapping({"/customer-service", "/customerService"})
    public String customerService() {
        return "customerService";
    }

    @GetMapping({"/apply-modify", "/applyModify"})
    public String applyModify() {
        return "applyModify";
    }

    @GetMapping({"/admin/login", "/adminlogin"})
    public String adminLogin() {
        return "adminlogin";
    }

    @GetMapping({"/admin/create", "/admincreate"})
    public String adminCreate() {
        return "admincreate";
    }

    @GetMapping({"/access-denied", "/accessdenied"})
    public String accessDenied() {
        return "accessdenied";
    }

    @GetMapping({"/admin/dashboard", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }
}
