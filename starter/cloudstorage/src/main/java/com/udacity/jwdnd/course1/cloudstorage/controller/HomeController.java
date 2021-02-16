package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;

//    private User fakeUser; //wip

    public HomeController(NoteService noteService, UserService userService) {
        this.userService = userService;
        this.noteService = noteService;

//        // wip
//        fakeUser.setFirstName("first");
//        fakeUser.setUserId(0);
//        fakeUser.setLastName("last");
//        fakeUser.setPassword("pass");
//        fakeUser.setUsername("user");
    }

    @GetMapping
    public String getHomePage(Model model) {
        return "home";
    }


}
