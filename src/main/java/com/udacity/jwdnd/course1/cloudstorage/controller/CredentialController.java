package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    private final CredentialsService credentialsService;

    public CredentialController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PostMapping("/credential")
    public String addCredential(@ModelAttribute Credentials credentials, RedirectAttributes redirectAttributes) {
        //Reuse the same method for add as well as edit
        if (credentials.getCredentialid() != null) {
            if (amendCredential(credentials)) {
                redirectAttributes.addFlashAttribute("message",
                        "Updated  " + credentials);
            } else {
                redirectAttributes.addFlashAttribute("message",
                        "Credentials Update failed !");
            }
        } else if (addCredential(credentials)) redirectAttributes.addFlashAttribute("message",
                "Added !" + credentials);
        else redirectAttributes.addFlashAttribute("message",
                    "Credentials Add failed !");
        return "redirect:/";
    }

    private boolean addCredential(Credentials credentials) {
        return credentialsService.addCredential(credentials);
    }

    private boolean amendCredential(Credentials credentials) {
        return credentialsService.updateCredential(credentials);
    }

    @GetMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credId, RedirectAttributes redirectAttributes) {
        credentialsService.deleteCredential(credId);
        redirectAttributes.addFlashAttribute("message",
                "Deleted Credential # " + credId + "!");
        return "redirect:/";
    }
}
