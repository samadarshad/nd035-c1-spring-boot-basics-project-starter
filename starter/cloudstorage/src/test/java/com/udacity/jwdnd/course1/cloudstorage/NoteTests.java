package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:NoteTests"})
@Transactional
public class NoteTests {

    @Autowired
    private NoteService noteService;

    private static User user;

    @BeforeAll
    public static void BeforeAll(@Autowired UserService userService) {
        user = new User(null, "user", null, "pass", "first", "last");
        userService.createUserAndUpdateObject(user);
    }

    @Test
    public void createAndRead() {
        Note note = new Note(null, "title", "description", user.getUserId());
        assertNull(note.getNoteId());
        noteService.createNoteAndUpdateObject(note);
        assertNotNull(note.getNoteId());
    }

    @Test
    public void createAndDelete() {
        Note note = new Note(null, "title", "description", user.getUserId());
        noteService.createNoteAndUpdateObject(note);

        noteService.deleteNote(note.getNoteId());
        Note deletedNote = noteService.getNote(note.getNoteId());
        assertNull(deletedNote);
    }
}
