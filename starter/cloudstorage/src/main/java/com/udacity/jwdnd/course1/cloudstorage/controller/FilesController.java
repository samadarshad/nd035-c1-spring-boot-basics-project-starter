package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import com.udacity.jwdnd.course1.cloudstorage.utility.Utils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@Controller
@RequestMapping("/files")
public class FilesController {

    private UserService userService;
    private FileService fileService;
    private Utils utils;

    public FilesController(UserService userService, FileService fileService, Utils utils) {

        this.userService = userService;
        this.fileService = fileService;
        this.utils = utils;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> get(@PathVariable("id") Integer id, Model model) {
        File file = fileService.get(id);
        User user = userService.get("user1"); //get this from auth
        utils.checkItemExistsAndUserIsAuthorizedOrThrowError(file, user);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                        + file.getFileName()
                        + "\""
                )
                .body(new ByteArrayResource(file.getFileData()));
    }

    @PostMapping
    public String add(@RequestParam("fileUpload") MultipartFile fileUpload, Model model
    ) throws IOException
    {
        User user = userService.get("user1"); //get this from auth
        if(fileUpload.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No file selected to upload."
            );
        }
        System.out.println("uploading" + fileUpload.getOriginalFilename());
        File file = new File(null,
                fileUpload.getOriginalFilename(),
                fileUpload.getContentType(),
                fileUpload.getSize(),
                user.getUserId(),
                fileUpload.getBytes());

        fileService.createAndUpdateObject(file);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id, Model model) {
        File existingFile = fileService.get(id);
        User user = userService.get("user1"); //get this from auth
        utils.checkItemExistsAndUserIsAuthorizedOrThrowError(existingFile, user);
        fileService.delete(id);
        return "redirect:/";
    }

}
