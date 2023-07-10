package com.ostasz.ads.user.service;

import com.ostasz.ads.user.datamodel.dto.request.ContactRequest;
import com.ostasz.ads.user.datamodel.dto.request.UserRequest;
import com.ostasz.ads.user.datamodel.dto.response.UserResponse;
import com.ostasz.ads.user.datamodel.entity.Contact;
import com.ostasz.ads.user.datamodel.entity.User;
import com.ostasz.ads.user.exception.FileExportException;
import com.ostasz.ads.user.exception.UserAlreadyExistsException;
import com.ostasz.ads.user.exception.UserWithPeselNotFoundException;
import com.ostasz.ads.user.repository.UserRepository;
import com.ostasz.ads.user.service.export.UserDetailsExportService;
import com.ostasz.ads.user.service.generator.ContactGenerator;
import com.ostasz.ads.user.service.generator.UserGenerator;
import com.ostasz.ads.user.validator.PeselValidator;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Integer DEFAULT_AMOUNT = 15000;
    private final UserRepository userRepository;
    private final PeselValidator peselValidator;
    private final ContactService contactService;
    private final UserDetailsExportService userDetailsExportService;
    private final UserGenerator userGenerator;
    private final ContactGenerator contactGenerator;

    public UserService(UserRepository userRepository, PeselValidator peselValidator, ContactService contactService,
                       UserDetailsExportService userDetailsExportService, UserGenerator userGenerator,
                       ContactGenerator contactGenerator) {
        this.userRepository = userRepository;
        this.peselValidator = peselValidator;
        this.contactService = contactService;
        this.userDetailsExportService = userDetailsExportService;
        this.userGenerator = userGenerator;
        this.contactGenerator = contactGenerator;
    }

    public UserResponse createUser(UserRequest userRequest) {
        String pesel = userRequest.getPesel();
        peselValidator.validate(pesel);
        checkIfUserWithPeselAlreadyExists(pesel);
        User user = createNewUser(userRequest, pesel);
        return new UserResponse(user);
    }

    public void addUserContact(Long userId, ContactRequest contactRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));
        Contact contact = contactService.createContact(contactRequest);
        updateUserContact(user, contact);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllWithContact()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserByPesel(String pesel) {
        peselValidator.validate(pesel);
        Optional<User> optionalUser = userRepository.findByPesel(pesel);

        if (optionalUser.isPresent()) {
            return new UserResponse(optionalUser.get());
        } else {
            throw new UserWithPeselNotFoundException();
        }
    }

    public Resource exportUsersToFile() throws FileExportException {
        return userDetailsExportService.exportUsersToFile();
    }

    public void generateRandomUsers() {
        List<Contact> savedContacts = contactGenerator.generateAndSaveContacts(DEFAULT_AMOUNT);
        userGenerator.generateAndSaveUsers(savedContacts, DEFAULT_AMOUNT);
    }

    private User createNewUser(UserRequest userRequest, String pesel) {
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .pesel(pesel)
                .build();
        return userRepository.save(user);
    }

    private void updateUserContact(User user, Contact contact) {
        userRepository.updateContactById(contact, user.getId());
    }

    private void checkIfUserWithPeselAlreadyExists(String pesel) {
        Optional<User> existingUser = userRepository.findByPesel(pesel);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
    }

}
