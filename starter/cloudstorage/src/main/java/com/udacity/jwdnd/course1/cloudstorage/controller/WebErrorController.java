package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
        import org.springframework.http.HttpStatus;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.RequestDispatcher;
        import javax.servlet.http.HttpServletRequest;

import static javax.servlet.RequestDispatcher.*;

@Controller
@RequestMapping("/error")
public class WebErrorController implements ErrorController {
    @GetMapping()
    public void handleError(HttpServletRequest request) throws ResponseStatusException {
        Integer errorStatusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE);
        if (errorStatusCode != null) {
            HttpStatus status = HttpStatus.valueOf(errorStatusCode);

            throw new ResponseStatusException(
                    status,
                    request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString()
            );
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."
        );
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}