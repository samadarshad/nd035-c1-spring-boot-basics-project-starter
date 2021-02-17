package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:FileTests"})
@Transactional
public class FileTests {

    @Autowired
    private FileService fileService;

    private static User user;
    private File file;
    private static byte [] fileData = "Hello World".getBytes(StandardCharsets.UTF_8);

    @BeforeAll
    public static void BeforeAll(@Autowired UserService userService) {
        user = new User(null, "user", null, "pass", "first", "last");
        userService.createAndUpdateObject(user);
    }

    @BeforeEach
    public void create() {

        file = new File(null, "fileName", "contentType", (long) 10, user.getUserId(), fileData);
        fileService.createAndUpdateObject(file);
    }

    @Test
    public void read() {
        assertNotNull(file.getFileId());
        assertEquals("fileName", fileService.get(file.getFileId()).getFileName());
        assertEquals("contentType", fileService.get(file.getFileId()).getContentType());
        assertEquals((long) 10, fileService.get(file.getFileId()).getFileSize());
        assertArrayEquals(fileData, fileService.get(file.getFileId()).getFileData());
    }

}

