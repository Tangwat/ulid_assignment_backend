package com.upperlink.ulidappportal.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ErrorHandlerController implements ErrorController {
    @RequestMapping("/error")
    @ResponseBody
    public String getErrorPath() {
        return "<center>" +
                "<h1>Something went wrong</h1>" +
                "<hr><br>" +
                "<div><h5>Go to <a href='/'>Ulid `Application Portal API</a> home page<h5>" +
                "</div>" +
                "</center>";
    }
}
