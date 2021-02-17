package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:NoteTests"})
@Transactional
public class NoteTests {

    @Autowired
    private NoteService noteService;

    private static User user;
    private Note note;

    @BeforeAll
    public static void BeforeAll(@Autowired UserService userService) {
        user = new User(null, "user", null, "pass", "first", "last");
        userService.createAndUpdateObject(user);
    }

    @BeforeEach
    public void create() {
        note = new Note(null, "title", "description", user.getUserId());
        noteService.createAndUpdateObject(note);
    }

    @Test
    public void read() {
        assertNotNull(note.getNoteId());
    }

    @Test
    public void delete() {
        noteService.delete(note.getNoteId());
        Note deletedNote = noteService.get(note.getNoteId());
        assertNull(deletedNote);
    }
}
