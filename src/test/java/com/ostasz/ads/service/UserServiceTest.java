package com.ostasz.ads.service;

import com.ostasz.ads.user.datamodel.dto.request.ContactRequest;
import com.ostasz.ads.user.datamodel.dto.request.UserRequest;
import com.ostasz.ads.user.datamodel.entity.User;
import com.ostasz.ads.user.exception.UserAlreadyExistsException;
import com.ostasz.ads.user.repository.UserRepository;
import com.ostasz.ads.user.service.ContactService;
import com.ostasz.ads.user.service.UserService;
import com.ostasz.ads.user.service.export.UserDetailsExportService;
import com.ostasz.ads.user.service.generator.ContactGenerator;
import com.ostasz.ads.user.service.generator.UserGenerator;
import com.ostasz.ads.user.validator.PeselValidator;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PeselValidator peselValidator;
    @Mock
    private ContactService contactService;
    @Mock
    private UserDetailsExportService userDetailsExportService;
    @Mock
    private UserGenerator userGenerator;
    @Mock
    private ContactGenerator contactGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, peselValidator, contactService, userDetailsExportService,
                userGenerator, contactGenerator);
    }

    @Test
    @Description("Should throw exception when creating user that already exists.")
    public void createAlreadyExistedUser() {
        UserRequest userRequest = new UserRequest("John", "Doe", "1234567890");
        String pesel = "1234567890";

        doNothing().when(peselValidator).validate(userRequest.getPesel());
        when(userRepository.findByPesel(pesel)).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(userRequest);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @Description("Verifies that a new contact is not added to user that does not exist")
    void addUserContactToNotExistingUser() {
        Long userId = 1L;

        String homeAddress = "Street 1/123 33-345 Poland";
        String registeredAddress = "Street 2/456 66-003 Poland";
        String email = "test@example.com";
        String privatePhoneNumber = "123456789";
        String businessPhoneNumber = "987654321";

        ContactRequest contactRequest = new ContactRequest(email, homeAddress, registeredAddress, privatePhoneNumber,
                businessPhoneNumber);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            userService.addUserContact(userId, contactRequest);
        });
        verify(contactService, never()).createContact(any(ContactRequest.class));
        verify(userRepository, never()).save(any(User.class));
    }

}

