package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public String handleStatusException(ResponseStatusException ex, Model model) {
        model.addAttribute("success",false);
        model.addAttribute("message", ex.getStatus() + ": " + ex.getReason());
        return "result";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleUploadSizeException(MaxUploadSizeExceededException ex, Model model) {
        model.addAttribute("success",false);
        model.addAttribute("message", ex.getMessage());
        return "result";
    }

}
