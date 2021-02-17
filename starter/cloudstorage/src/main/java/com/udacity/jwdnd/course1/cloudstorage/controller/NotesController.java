package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utility.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private UserService userService;
    private NoteService noteService;

    public NotesController(UserService userService, NoteService noteService) {
        this.userService=userService;
        this.noteService=noteService;
    }

    @PostMapping
    public String addOrEdit(
            @RequestParam("noteId") @Nullable Integer noteId,
            @RequestParam("noteTitle") String noteTitle,
            @RequestParam("noteDescription") String noteDescription,
            Model model
    ) {
        User user = userService.get("user1"); //get this from auth
        if (noteId == null) {
            Note note = new Note(null, noteTitle, noteDescription, user.getUserId());
            noteService.createAndUpdateObject(note);
        } else {
            Note note = new Note(noteId, noteTitle, noteDescription, user.getUserId());
            Note existingNote = noteService.get(note.getNoteId());
            Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(existingNote, user);
            noteService.update(note);
        }
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer noteId, Model model) {
        Note existingNote = noteService.get(noteId);
        User user = userService.get("user1"); //get this from auth
        Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(existingNote, user);
        noteService.delete(noteId);
        return "redirect:/";
    }

}
