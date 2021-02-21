package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private FileService fileService;
    private CredentialService credentialService;

    public HomeController(NoteService noteService,
                          UserService userService,
                          FileService fileService,
                          CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getHomePage(Model model,
                              Authentication auth
    ) {
        User user = this.userService.get(auth.getName());
        if (user == null) {
            return "login";
        }
        model.addAttribute("notes", noteService.getByUserId(user.getUserId()));
        model.addAttribute("files", fileService.getByUserId(user.getUserId()));
        model.addAttribute("credentials", credentialService.getByUserId(user.getUserId()));
        return "home";
    }

}
