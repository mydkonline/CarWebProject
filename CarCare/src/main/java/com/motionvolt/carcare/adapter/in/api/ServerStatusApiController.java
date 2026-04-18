package com.motionvolt.carcare.adapter.in.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerStatusApiController {
    @GetMapping({"/", "/api"})
    public StatusResponse status() {
        return new StatusResponse("MotionVolt CarCare API", "React Native", "running");
    }

    public static class StatusResponse {
        private final String service;
        private final String frontend;
        private final String status;

        public StatusResponse(String service, String frontend, String status) {
            this.service = service;
            this.frontend = frontend;
            this.status = status;
        }

        public String getService() {
            return service;
        }

        public String getFrontend() {
            return frontend;
        }

        public String getStatus() {
            return status;
        }
    }
}
