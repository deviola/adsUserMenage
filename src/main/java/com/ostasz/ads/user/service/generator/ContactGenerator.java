package com.ostasz.ads.user.service.generator;

import com.github.javafaker.Faker;
import com.ostasz.ads.user.datamodel.entity.Contact;
import com.ostasz.ads.user.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactGenerator {

    private final ContactRepository contactRepository;
    private final Faker faker = new Faker();

    public ContactGenerator(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> generateAndSaveContacts(Integer totalAmount) {
        List<Contact> allContacts = new ArrayList<>();
        int batchSize = 1000;

        for (int i = 0; i < totalAmount; i += batchSize) {
            List<Contact> contacts = generateContacts(batchSize);
            List<Contact> savedContacts = contactRepository.saveAll(contacts);
            allContacts.addAll(savedContacts);
        }

        return allContacts;
    }

    private List<Contact> generateContacts(int batchSize) {
        List<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            Contact contact = generateContact();
            contacts.add(contact);
        }

        return contacts;
    }

    private Contact generateContact() {
        String email = faker.internet().emailAddress();
        String privatePhoneNumber = faker.phoneNumber().cellPhone();
        String businessPhoneNumber = faker.phoneNumber().cellPhone();
        String homeAddress = faker.address().fullAddress();
        String registeredAddress = faker.address().fullAddress();

        return Contact.builder()
                .email(email)
                .privatePhoneNumber(privatePhoneNumber)
                .businessPhoneNumber(businessPhoneNumber)
                .homeAddress(homeAddress)
                .registeredAddress(registeredAddress)
                .build();
    }

}
