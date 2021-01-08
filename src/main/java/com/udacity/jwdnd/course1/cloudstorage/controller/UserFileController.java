package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.StorageFileNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.services.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class UserFileController {
    private final UserFileService userFileService;

    @Autowired
    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/files/view/{id}")
    @ResponseBody
    public ResponseEntity serveFile(@PathVariable Integer id) throws SQLException {
        UserFile file = userFileService.getUserFileById(id);
        String contentType = file.getContenttype();
        String fileName = file.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFiledata());
    }

    @GetMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        var filename = userFileService.getUserFileById(id).getFilename();
        boolean deleted = userFileService.deleteFileByFileIdAndUserId(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message",
                    "The file  " + filename + " Deleted!");
        } else redirectAttributes.addFlashAttribute("message",
                "Error deleting  " + filename + " !");
        return "redirect:/";
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException, SQLException {
        // Check for empty file
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    "No file to upload " + file.getOriginalFilename() + "!");
            return "redirect:/";
        }
        //Check for file size
        if (file.getSize() > 128000) {
            redirectAttributes.addFlashAttribute("message",
                    "Maximum file size = 128KB, you tried uploading " + file.getSize() + " Bytes!");
            return "redirect:/";
        }
        //Check whether same file exists
        if (userFileService.doSameFileNameExists(file.getOriginalFilename()) > 0) {
            redirectAttributes.addFlashAttribute("message",
                    "The file with  name " + file.getOriginalFilename() + " exists");
            return "redirect:/";
        }
        byte[] fileContent = file.getBytes();
        UserFile userFile = new UserFile
                (null,
                        file.getOriginalFilename(),
                        file.getContentType(),
                        Long.toString(file.getSize()),
                        userFileService.getFileOwnerUserId(),
                        fileContent);
        userFileService.addUserFile(userFile);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}