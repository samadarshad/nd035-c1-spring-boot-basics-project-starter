package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private UserService userService;
    private NoteService noteService;

//    private User mockUser; //wip

    public NotesController(UserService userService, NoteService noteService) {
    this.userService=userService;
    this.noteService=noteService;

//    this.mockUser = userService.getUser("user");
    }

    @PostMapping
    public String addOrEditNote(
            @RequestParam("noteId") @Nullable Integer noteId,
            @RequestParam("noteTitle") String noteTitle,
            @RequestParam("noteDescription") String noteDescription
    ) {
        User user = userService.getUser("user");;
        if (noteId == null) {
            System.out.println("add note" + noteTitle + noteDescription);
            Note note = new Note(null, noteTitle, noteDescription, user.getUserId());
            noteService.createNoteAndUpdateObject(note);
        } else {
            System.out.println("edit note" + noteId.toString() + noteTitle + noteDescription);
            Note note = new Note(noteId, noteTitle, noteDescription, user.getUserId());
            noteService.updateNote(note);
        }
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Integer id, Model model) {
        System.out.println("deleting note id: " + id);
        return "redirect:/";
    }
}
