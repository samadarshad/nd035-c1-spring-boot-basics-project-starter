package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
