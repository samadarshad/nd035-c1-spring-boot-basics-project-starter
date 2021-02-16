package com.udacity.jwdnd.course1.cloudstorage;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserTests {
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void whenAddUserToDatabaseThenTheirUsernameIsNotAvailable() throws InterruptedException {
        assertTrue(userService.isUsernameAvailable("user"));
        User user = new User(null, "user", null, "pass", "first", "last");
        userService.createUserAndUpdateObject(user);
        assertFalse(userService.isUsernameAvailable("user"));
    }

    @Test
    @Transactional
    public void whenAddUserThenTheirPasswordIsHashed() throws InterruptedException {
        User user = new User(null, "user", null, "pass", "first", "last");

        assertEquals("pass", user.getPassword());
        userService.createUserAndUpdateObject(user);
        assertNotEquals("pass", user.getPassword());
    }

    @Test
    @Transactional
    public void whenAddUserThenCanGetSameUserById() throws InterruptedException {
        User user = new User(null, "user", null, "pass", "first", "last");
        userService.createUserAndUpdateObject(user);

        int userId = user.getUserId();
        User sameUser = userService.getUserById(userId);
        assertEquals(user.getUsername(), sameUser.getUsername());
    }
}
