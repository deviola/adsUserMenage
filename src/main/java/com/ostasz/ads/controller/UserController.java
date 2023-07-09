package com.ostasz.ads.controller;

import com.ostasz.ads.datamodel.dto.ContactDTO;
import com.ostasz.ads.datamodel.dto.UserDTO;
import com.ostasz.ads.datamodel.entity.User;
import com.ostasz.ads.service.UserService;
import com.ostasz.ads.service.export.UserDetailsExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDetailsExportService userDetailsExportService;

    public UserController(UserService userService, UserDetailsExportService userDetailsExportService) {
        this.userService = userService;
        this.userDetailsExportService = userDetailsExportService;
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PostMapping(value = "/addContact/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> addUserContact(@PathVariable long userId, @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(userService.addUserContact(userId, contactDTO));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = "/{pesel}")
    public ResponseEntity<User> getUserByPesel(@PathVariable String pesel) {
        return ResponseEntity.ok(userService.getUserByPesel(pesel));
    }

    @GetMapping(value = "/export")
    public ResponseEntity<Resource> exportUsersToFile() throws IOException {
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
        userService.generateAndImportRandomUsers();
        return ResponseEntity.ok("Users generated and imported successfully.");
    }

}