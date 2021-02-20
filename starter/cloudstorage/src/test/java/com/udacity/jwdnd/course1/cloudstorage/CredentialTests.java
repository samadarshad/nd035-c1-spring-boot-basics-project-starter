package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:CredentialTests"})
@Transactional
public class CredentialTests {

    @Autowired
    private CredentialService credentialService;

    private static User user;
    private Credential credential;

    @BeforeAll
    public static void BeforeAll(@Autowired UserService userService) {
        user = new User(null, "user", null, "pass", "first", "last");
        userService.createAndUpdateObject(user);
    }

    @BeforeEach
    public void create() {
        credential = new Credential(null, "url", "username", null, "password", user.getUserId());
        credentialService.createAndUpdateObject(credential);
    }

    @Test
    public void createEncryptsPassword() {
        assertNotNull(credential.getCredentialId());
        assertNotNull(credential.getKey());
        assertNotEquals("password", credential.getPassword());
    }

    @Test
    public void getDecryptsPassword() {
        assertEquals("url", credentialService.get(credential.getCredentialId()).getUrl());
        assertEquals("username", credentialService.get(credential.getCredentialId()).getUsername());
        assertEquals("password", credentialService.get(credential.getCredentialId()).getPassword());
    }

    @Test
    public void getAllByUserIsWithEncryptedPassword() {
        assertEquals("url", credentialService.getByUserId(credential.getUserId()).get(0).getUrl());
        assertEquals("username", credentialService.getByUserId(credential.getUserId()).get(0).getUsername());
        assertNotEquals("password", credentialService.getByUserId(credential.getUserId()).get(0).getPassword());
    }

    @Test
    public void update() {
        credential.setUrl("url2");
        credential.setUsername("username2");
        credential.setPassword("password2");
        Integer credentialId = credential.getCredentialId();
        credentialService.update(credential);

        assertEquals(credentialId, credential.getCredentialId());
        assertEquals("url2", credentialService.get(credential.getCredentialId()).getUrl());
        assertEquals("username2", credentialService.get(credential.getCredentialId()).getUsername());
        assertEquals("password2", credentialService.get(credential.getCredentialId()).getPassword());
    }


    @Test
    public void delete() {
        credentialService.delete(credential.getCredentialId());
        Credential deletedCredential = credentialService.get(credential.getCredentialId());
        assertNull(deletedCredential);
    }

}
