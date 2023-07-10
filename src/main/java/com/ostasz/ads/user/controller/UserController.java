package com.ostasz.ads.user.controller;

import com.ostasz.ads.user.datamodel.dto.request.ContactRequest;
import com.ostasz.ads.user.datamodel.dto.request.UserRequest;
import com.ostasz.ads.user.datamodel.dto.response.UserResponse;
import com.ostasz.ads.user.exception.FileExportException;
import com.ostasz.ads.user.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PostMapping(value = "/{userId}/add-contact", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> addUserContact(@PathVariable long userId, @RequestBody ContactRequest contactRequest) {
        userService.addUserContact(userId, contactRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = "/first-by-pesel", params = "pesel")
    public ResponseEntity<UserResponse> getUserByPesel(@RequestParam String pesel) {
        return ResponseEntity.ok(userService.getUserByPesel(pesel));
    }

    @GetMapping(value = "/export")
    public ResponseEntity<Resource> exportUsersToFile() throws FileExportException, IOException {
        Resource fileResource = userService.exportUsersToFile();
        InputStreamResource resource = new InputStreamResource(fileResource.getInputStream());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateRandomUsers() {
        userService.generateRandomUsers();
        return ResponseEntity.ok("Users generated successfully.");
    }

}