package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ResponseStatusException.class)
    String handleStatusException(ResponseStatusException ex, Model model) {
        model.addAttribute("success",false);
        model.addAttribute("message", ex.getStatus() + ": " + ex.getReason());
        return "result";
    }
}
