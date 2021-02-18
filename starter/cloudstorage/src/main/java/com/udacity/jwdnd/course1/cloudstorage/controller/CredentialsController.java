package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utility.Utils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {

    private UserService userService;
    private CredentialService credentialService;

    public CredentialsController(UserService userService, CredentialService credentialService) {

        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Credential get(@PathVariable("id") Integer id, Model model,
                          Authentication auth
    ) {
        User user = this.userService.get(auth.getName());
        Credential credential = credentialService.get(id);
        Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(credential, user);
        return credential;
        //display decrypted creds to user
    }

    @PostMapping
    public String addOrEdit(
            @RequestParam("credentialId") @Nullable Integer credentialId,
            @RequestParam("url") String url,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            Authentication auth
    ) {
        User user = this.userService.get(auth.getName());
        if (credentialId == null) {
            Credential credential = new Credential(null, url, username, null, password, user.getUserId());
            credentialService.createAndUpdateObject(credential);
        } else {
            Credential credential = new Credential(credentialId, url, username, null, password, user.getUserId());

            Credential existingCredential = credentialService.get(credential.getCredentialId());
            Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(existingCredential, user);
            credentialService.update(credential);
        }
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id, Model model,
                         Authentication auth
    ) {
        User user = this.userService.get(auth.getName());
        Credential credential = credentialService.get(id);
        Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(credential, user);
        credentialService.delete(id);
        return "redirect:/";
    }

}
