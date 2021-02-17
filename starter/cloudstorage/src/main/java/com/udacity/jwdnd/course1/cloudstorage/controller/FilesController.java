package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

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

    public FilesController(UserService userService, FileService fileService) {

        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> get(@PathVariable("id") Integer id, Model model) {
        File file = fileService.get(id);
        User user = userService.get("user1"); //get this from auth
        if (file == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "File does not exist."
            );
//            return error404(model);
        }
//
        if (file.getUserId() != user.getUserId()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User is not authorized to access that file."
            );
//            return ResponseEntity.status(401);
        }
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
            return error400(model);
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
        if (existingFile == null) {
            return error404(model);
        };

        if (existingFile.getUserId() != user.getUserId()) {
            return error401(model);
        }
        fileService.delete(id);
        return "redirect:/";
    }

    private String error400(Model model) {
        model.addAttribute("success",false);
        model.addAttribute("message","400: No file selected to upload.");
        return "result";
    }

    private String error404(Model model) {
        model.addAttribute("success",false);
        model.addAttribute("message","404: File does not exist.");
        return "result";
    }

    private String error401(Model model) {
        model.addAttribute("success",false);
        model.addAttribute("message","401: User is not authorized to access that file.");
        return "result";
    }

}
