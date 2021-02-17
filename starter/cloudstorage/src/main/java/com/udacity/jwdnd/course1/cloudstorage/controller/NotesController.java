package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @PostMapping
    public String addNote(@RequestParam("noteTitle") String noteTitle,
                          @RequestParam("noteDescription") String noteDescription
    ) {
        System.out.println("adding note" + noteTitle + noteDescription);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Integer id, Model model) {
        System.out.println("deleting note id: " + id);
        return "redirect:/";
    }
}
