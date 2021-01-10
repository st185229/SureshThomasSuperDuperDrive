package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// This class create a security hole, but needs to be removed after testing
@Controller()
@RequestMapping("/deleteTestOnlyUsers")
public class ApplicationTestingOnlyController {

    private final UserService userService;
    private final Environment env;

    public ApplicationTestingOnlyController(UserService userService, Environment env) {
        this.userService = userService;
        this.env =env;
    }

    @GetMapping()
    public String deleteTestUsers(@RequestParam String token) {

        if(token==null || token.length() <16) return "login";
        //Read from properies
        String testingOnlyTokenFromProperties = env.getProperty("application.token.for.testing.only");
        //Removing the token from application.properties  or replace with a small string remove the access to the service
        if(testingOnlyTokenFromProperties == null || testingOnlyTokenFromProperties.length() < 16) return "login";
        if(testingOnlyTokenFromProperties.equals(token)) userService.deleteTestUsers();

        return "redirect:/login?logout";
    }
}
