package com.ostasz.ads.service;

import com.ostasz.ads.datamodel.dto.ContactDTO;
import com.ostasz.ads.datamodel.dto.UserDTO;
import com.ostasz.ads.datamodel.entity.Address;
import com.ostasz.ads.datamodel.entity.Contact;
import com.ostasz.ads.datamodel.entity.User;
import com.ostasz.ads.exception.UserAlreadyExistsException;
import com.ostasz.ads.repository.ContactRepository;
import com.ostasz.ads.repository.UserRepository;
import com.ostasz.ads.service.export.UserDetailsExportService;
import com.ostasz.ads.service.generator.AddressGenerator;
import com.ostasz.ads.service.generator.ContactGenerator;
import com.ostasz.ads.service.generator.UserGenerator;
import com.ostasz.ads.validator.PeselValidator;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static final Integer DEFAULT_AMOUNT = 15000;
    private static final String USER_FILE_PATH = "users.csv";
    private final UserRepository userRepository;
    private final PeselValidator peselValidator;
    private final ContactService contactService;
    private final UserDetailsExportService userDetailsExportService;
    private final UserGenerator userGenerator;
    private final ContactGenerator contactGenerator;
    private final AddressGenerator addressGenerator;
    private final ContactRepository contactRepository;

    public UserService(UserRepository userRepository, PeselValidator peselValidator, ContactService contactService,
                       UserDetailsExportService userDetailsExportService, UserGenerator userGenerator, ContactGenerator contactGenerator,
                       AddressGenerator addressGenerator, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.peselValidator = peselValidator;
        this.contactService = contactService;
        this.userDetailsExportService = userDetailsExportService;
        this.userGenerator = userGenerator;
        this.contactGenerator = contactGenerator;
        this.addressGenerator = addressGenerator;
        this.contactRepository = contactRepository;
    }

    public User createUser(UserDTO userDTO) {
        String pesel = peselValidator.validate(userDTO.getPesel());
        checkIfUserAlreadyExists(pesel);

        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .pesel(pesel)
                .build();

        userRepository.save(user);
        return user;
    }

    public User addUserContact(Long userId, ContactDTO contactDTO) {
        Optional<User> optionalUser = Optional.of(userRepository.findById(userId))
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        optionalUser.ifPresent(user -> {
            Contact contact = contactService.createContact(contactDTO);
            user.setContact(contact);
            userRepository.save(user);
        });
        return optionalUser.get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAllWithContactAndAddress();
    }

    public User getUserByPesel(String pesel) {
        return userRepository.findByPesel(peselValidator.validate(pesel));
    }

    public Resource exportUsersToFile() {
        return userDetailsExportService.exportUsersToFile();
    }

    public void generateAndImportRandomUsers() {
        List<User> savedUsers = generateAndSaveUsers();
        List<Contact> savedContacts = contactGenerator.generateAndSaveContacts(DEFAULT_AMOUNT);
        List<Address> savedAddresses = addressGenerator.generateAndSaveAddresses(DEFAULT_AMOUNT);
        assignUserDetails(savedUsers, savedContacts, savedAddresses);
    }

    private void checkIfUserAlreadyExists(String pesel) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByPesel(pesel));
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
    }

    private List<User> generateAndSaveUsers() {
        userGenerator.generateUsersToFile(USER_FILE_PATH, DEFAULT_AMOUNT);
        List<User> users = userGenerator.generateUsersFromCsvFile(USER_FILE_PATH);
        return userRepository.saveAll(users);
    }

    private void assignUserDetails(List<User> savedUsers, List<Contact> savedContacts, List<Address> savedAddresses) {
        if (savedContacts.isEmpty()) {
            return;
        }

        for (User user : savedUsers) {
            int randomContactIndex = new Random().nextInt(savedContacts.size());
            Contact contact = savedContacts.get(randomContactIndex);

            int randomAddressIndex = new Random().nextInt(savedAddresses.size());
            Address address = savedAddresses.get(randomAddressIndex);

            contact.setHomeAddress(address);
            contact.setRegisteredAddress(address);

            user.setContact(contact);
        }

        contactRepository.saveAll(savedContacts);
        userRepository.saveAll(savedUsers);
    }

}
