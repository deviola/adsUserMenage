package com.ostasz.ads.service;

import com.ostasz.ads.datamodel.dto.AddressDTO;
import com.ostasz.ads.datamodel.dto.ContactDTO;
import com.ostasz.ads.datamodel.dto.UserDTO;
import com.ostasz.ads.datamodel.entity.User;
import com.ostasz.ads.exception.UserAlreadyExistsException;
import com.ostasz.ads.repository.ContactRepository;
import com.ostasz.ads.repository.UserRepository;
import com.ostasz.ads.service.export.UserDetailsExportService;
import com.ostasz.ads.service.generator.AddressGenerator;
import com.ostasz.ads.service.generator.ContactGenerator;
import com.ostasz.ads.service.generator.UserGenerator;
import com.ostasz.ads.validator.PeselValidator;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private AddressGenerator addressGenerator;
    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, peselValidator, contactService, userDetailsExportService,
                userGenerator, contactGenerator, addressGenerator, contactRepository);
    }

    @Test
    @Description("Should correctly create new user.")
    public void correctlyCreateUser() {
        UserDTO userDTO = new UserDTO("John", "Doe", "1234567890");
        String pesel = "1234567890";
        User expectedUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .pesel(pesel)
                .build();

        when(peselValidator.validate(userDTO.getPesel())).thenReturn(pesel);
        when(userRepository.findByPesel(pesel)).thenReturn(null);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(expectedUser.getFirstName(), createdUser.getFirstName());
        assertEquals(expectedUser.getLastName(), createdUser.getLastName());
        assertEquals(expectedUser.getPesel(), createdUser.getPesel());
        verify(userRepository, times(1)).save(createdUser);
    }

    @Test
    @Description("Should throw exception when creating user that already exists.")
    public void createAlreadyExistedUser() {
        UserDTO userDTO = new UserDTO("John", "Doe", "1234567890");
        String pesel = "1234567890";

        when(peselValidator.validate(userDTO.getPesel())).thenReturn(pesel);
        when(userRepository.findByPesel(pesel)).thenReturn(new User());

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(userDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @Description("Correctly create new contact for existing user.")
    void addUserContactToExistingUser() {
        Long userId = 1L;

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setEmail("test@example.com");
        contactDTO.setHomeAddress(new AddressDTO("Street 1", "123", "A1", "Country", "12345"));
        contactDTO.setRegisteredAddress(new AddressDTO("Street 2", "456", "B2", "Country", "67890"));
        contactDTO.setPrivatePhoneNumber("123456789");
        contactDTO.setBusinessPhoneNumber("987654321");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            userService.addUserContact(userId, contactDTO);
        });
        verify(contactService, never()).createContact(any(ContactDTO.class));
        verify(userRepository, never()).save(any(User.class));
    }
}

