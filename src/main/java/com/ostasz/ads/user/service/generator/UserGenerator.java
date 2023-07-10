package com.ostasz.ads.user.service.generator;

import com.github.javafaker.Faker;
import com.ostasz.ads.user.datamodel.entity.Contact;
import com.ostasz.ads.user.datamodel.entity.User;
import com.ostasz.ads.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGenerator {

    private final PeselGenerator peselGenerator;
    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    public UserGenerator(PeselGenerator peselGenerator, UserRepository userRepository) {
        this.peselGenerator = peselGenerator;
        this.userRepository = userRepository;
    }

    public List<User> generateAndSaveUsers(List<Contact> savedContacts, Integer totalAmount) {
        List<User> allUsers = new ArrayList<>();
        int batchSize = 1000;

        for (int i = 0; i < totalAmount; i += batchSize) {
            List<User> users = generateUsers(batchSize, savedContacts);
            List<User> savedUsers = userRepository.saveAll(users);
            allUsers.addAll(savedUsers);
        }
        return allUsers;
    }

    private List<User> generateUsers(int batchSize, List<Contact> savedContacts) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            User user = generateUser(savedContacts);
            users.add(user);
        }
        return users;
    }

    private User generateUser(List<Contact> savedContacts) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String pesel = peselGenerator.generateRandomPesel(faker);
        Contact contact = getRandomContact(savedContacts);

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .pesel(pesel)
                .contact(contact)
                .build();
    }

    private Contact getRandomContact(List<Contact> savedContacts) {
        int index = faker.random().nextInt(savedContacts.size());
        return savedContacts.get(index);
    }
}
