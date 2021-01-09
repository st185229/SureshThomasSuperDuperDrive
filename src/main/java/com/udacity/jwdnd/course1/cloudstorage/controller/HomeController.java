package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFileMetadata;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final UserFileService userFileService;
    private final NotesService notesService;
    private final CredentialsService credentialsService;
    private final EncryptionService encryptionService;

    @Autowired
    public HomeController(UserFileService userFileService, NotesService notesService, CredentialsService credentialsService, EncryptionService encryptionService) {
        this.userFileService = userFileService;
        this.notesService = notesService;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String listUploadedFiles(Model model) throws IOException {
        List<UserFileMetadata> fileList = userFileService.getCurrentUserFilesMetaData();
        model.addAttribute("files", fileList);
        List<Notes> notesList = notesService.getNotes();
        model.addAttribute("notes", notesList);
        model.addAttribute("cryptoUtil", encryptionService);
        List<Credentials> credentialsList = credentialsService.getCredentials();
        model.addAttribute("storedCredentials", credentialsList);

        return "home";
    }
}
