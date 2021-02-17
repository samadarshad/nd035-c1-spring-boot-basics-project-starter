package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserService userService;
    private NoteService noteService;

    private User mockUser; //wip

    public HomeController(NoteService noteService, UserService userService) {
        this.userService = userService;
        this.noteService = noteService;

        // wip
        User mockUser1 = new User(null, "user1", null, "pass", "first", "last");
        this.userService.createAndUpdateObject(mockUser1);
        Note note = new Note(null, "title1", "description1", mockUser1.getUserId());
        this.noteService.createAndUpdateObject(note);
        note = new Note(null, "title2", "description2", mockUser1.getUserId());
        this.noteService.createAndUpdateObject(note);


        User mockUser2 = new User(null, "user2", null, "pass", "first", "last");
        this.userService.createAndUpdateObject(mockUser2);
        note = new Note(null, "title1_user2", "description1", mockUser2.getUserId());
        this.noteService.createAndUpdateObject(note);
        note = new Note(null, "title2_user2", "description2", mockUser2.getUserId());
        this.noteService.createAndUpdateObject(note);
    }


    @GetMapping
    public String getHomePage(Model model) {
        User user = userService.get("user1"); //get this from auth
        model.addAttribute("notes", noteService.getByUserId(user.getUserId()));
        return "home";
    }

//    @DeleteMapping("/notes/{id}")
//    public String deleteNote(@PathVariable Integer id, Model model) {
//        System.out.println("deleting note id: " + id);
//        return "redirect:/home";
//    }


}
