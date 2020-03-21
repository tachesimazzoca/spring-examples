package com.github.tachesimazzoca.spring.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class ApiController {
    @RequestMapping(
        value = "/config"
      , method = RequestMethod.GET
    )
    @ResponseBody
    public ApiResponse config() {
        ApiResponse<List<String>> response = new ApiResponse<List<String>>(
            Arrays.asList(new String[] {"1st", "2nd", "3rd"})
        );
        return response;
    }

    public class ApiResponse<T> {
        private int status;
        private String[] messages;
        private T entry;

        private ApiResponse() {
            this.status = 200;
            this.messages = new String[] {};
        }

        public ApiResponse(T entry) {
            this();
            this.entry = entry;
        }

        public ApiResponse(int status, String[] messages, T entry) {
            this.status = status;
            this.messages = messages;
            this.entry = entry;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String[] getMessages() {
            return messages;
        }

        public void setMessages(String[] messages) {
            this.messages = messages;
        }

        public T getEntry() {
            return entry;
        }

        public void setEntry(T entry) {
            this.entry = entry;
        }
    }
}
