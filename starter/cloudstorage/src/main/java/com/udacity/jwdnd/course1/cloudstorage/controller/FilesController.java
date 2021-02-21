package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import com.udacity.jwdnd.course1.cloudstorage.utility.Utils;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/files")
public class FilesController {

    private UserService userService;
    private FileService fileService;

    public FilesController(UserService userService, FileService fileService) {

        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> get(@PathVariable("id") Integer id, Model model,
                                        Authentication auth
    ) {
        User user = this.userService.get(auth.getName());
        File file = fileService.get(id);
        Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(file, user);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                        + file.getFileName()
                        + "\""
                )
                .body(new ByteArrayResource(file.getFileData()));
    }

    public Boolean userHasFilename(User user, String filename) {
        List<String> fileNames = fileService.getByUserId(user.getUserId()).stream().map(
                File::getFileName
        ).collect(Collectors.toList());
        return fileNames.contains(filename);
    }

    @PostMapping
    public String add(@RequestParam("fileUpload") MultipartFile fileUpload, Model model,
                      Authentication auth
    ) throws IOException {
        User user = this.userService.get(auth.getName());
        if(fileUpload.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No file selected to upload."
            );
        }

        if(userHasFilename(user, fileUpload.getOriginalFilename())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Filename already exists."
            );
        }

        System.out.println("uploading" + fileUpload.getOriginalFilename());
        File file = new File(null,
                fileUpload.getOriginalFilename(),
                fileUpload.getContentType(),
                fileUpload.getSize(),
                user.getUserId(),
                fileUpload.getBytes());


        fileService.createAndUpdateObject(file);
        return "redirect:/?success";


    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id, Model model,
                         Authentication auth
    ) {
        User user = this.userService.get(auth.getName());
        File existingFile = fileService.get(id);
        Utils.checkItemExistsAndUserIsAuthorizedOrThrowError(existingFile, user);
        fileService.delete(id);
        return "redirect:/?success";
    }

}
