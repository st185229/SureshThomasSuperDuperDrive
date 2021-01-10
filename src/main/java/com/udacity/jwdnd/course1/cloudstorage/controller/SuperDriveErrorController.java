package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SuperDriveErrorController implements ErrorController {


    @Override
    public String getErrorPath() {
        return "/error";
    }
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // TODO: log error details here

        if (status != null
                && model.getAttribute("generalError") != null
                && model.getAttribute("failedSavingChanges") != null
                && model.getAttribute("savedChanges") != null) {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("generalError", true);
                //return "404";
                return "result";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("generalError", true);
               // return "500";
                return "result";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("generalError", true);
                //return "403";
                return "result";
            }

        }
        model.addAttribute("generalError", true);
        return "result";
    }
}
