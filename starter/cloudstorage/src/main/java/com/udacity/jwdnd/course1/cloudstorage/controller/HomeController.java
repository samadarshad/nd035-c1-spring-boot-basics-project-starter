package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;

    private User mockUser; //wip

    public HomeController(NoteService noteService, UserService userService) {
        this.userService = userService;
        this.noteService = noteService;

        // wip
        this.mockUser = new User(null, "user", null, "pass", "first", "last");
        this.userService.createUserAndUpdateObject(this.mockUser);
        Note note = new Note(null, "title1", "description1", mockUser.getUserId());
        this.noteService.createNoteAndUpdateObject(note);
        note = new Note(null, "title2", "description2", mockUser.getUserId());
        this.noteService.createNoteAndUpdateObject(note);
    }


    @GetMapping
    public String getHomePage(Model model) {
        User user = this.mockUser; //get this from auth
        model.addAttribute("notes", noteService.getNotesByUserId(user.getUserId()));
        return "home";
    }

    @DeleteMapping("/notes/{id}")
    public String deleteNote(@PathVariable Integer id, Model model) {
        System.out.println("deleting note id: " + id);
        return "redirect:/home";
    }


}
