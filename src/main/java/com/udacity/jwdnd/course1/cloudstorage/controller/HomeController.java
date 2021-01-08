package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFileMetadata;
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

    @Autowired
    public HomeController(UserFileService userFileService, NotesService notesService) {
        this.userFileService = userFileService;
        this.notesService = notesService;
    }

    @GetMapping()
    public String listUploadedFiles(Model model) throws IOException {
        List<UserFileMetadata> fileList = userFileService.getCurrentUserFilesMetaData();
        model.addAttribute("files", fileList);
        List<Notes> notesList = notesService.getNotes();
        model.addAttribute("notes", notesList);
        return "home";
    }
}
