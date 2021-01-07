package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.StorageFileNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFileMetadata;
import com.udacity.jwdnd.course1.cloudstorage.services.StorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserFileController {

    private final StorageService storageService;

    private final UserFileService userFileService;

    @Autowired
    public UserFileController(StorageService storageService, UserFileService userFileService) {
        this.storageService = storageService;
        this.userFileService = userFileService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        List<UserFileMetadata> list = userFileService.getCurrentUserFilesMetaData();
        model.addAttribute("files",list);
        return "home";
    }

    @GetMapping("/files/view/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> serveFile(@PathVariable Integer id) {

        HttpHeaders headers = new HttpHeaders();
        byte[] media = userFileService.getUserFileId(id);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;

        /*
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

         */
    }

    @GetMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable Integer id) {
        boolean deleted = userFileService.deleteFileByFileIdAndUserId(id);

        return "redirect:/";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {



        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        byte[] fileContent = file.getBytes();

        UserFile userFile = new UserFile
                (null,
                        file.getOriginalFilename(),
                        file.getContentType(),
                        Long.toString(file.getSize()),
                        userFileService.getFileOwnerUserId(),
                        fileContent);
        userFileService.addUserFile(userFile);
        return "redirect:/";
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}