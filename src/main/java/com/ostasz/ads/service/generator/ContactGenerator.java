package com.ostasz.ads.service.generator;

import com.github.javafaker.Faker;
import com.ostasz.ads.datamodel.entity.Contact;
import com.ostasz.ads.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ContactGenerator {

    private static final String CONTACT_FILE_PATH = "contact.csv";
    private static final List<String> CONTACT_FIELDS = Arrays.asList(
            "email",
            "privatePhoneNumber",
            "businessPhoneNumber",
            "homeAddress",
            "registeredAddress"
    );
    private final ContactRepository contactRepository;

    public ContactGenerator(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> generateAndSaveContacts(Integer amount) {
        generateContactsToFile(amount);
        List<Contact> contacts = generateContactsFromCsvFile();
        return contactRepository.saveAll(contacts);
    }

    private void generateContactsToFile(int contactsAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONTACT_FILE_PATH))) {
            Faker faker = new Faker(new Locale("pl"));

            for (int i = 0; i < contactsAmount; i++) {
                Contact contact = generateRandomContact(faker);
                String line = contactToCsvLine(contact);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Contact generateRandomContact(Faker faker) {
        List<String> selectedFields = selectRandomFields();
        String email = null;
        String privatePhoneNumber = null;
        String businessPhoneNumber = null;

        for (String field : selectedFields) {
            switch (field) {
                case "email":
                    email = faker.internet().emailAddress();
                    break;
                case "privatePhoneNumber":
                    privatePhoneNumber = faker.phoneNumber().phoneNumber();
                    break;
                case "businessPhoneNumber":
                    businessPhoneNumber = faker.phoneNumber().phoneNumber();
                    break;
            }
        }
        return createNewContact(email, privatePhoneNumber, businessPhoneNumber);
    }

    private Contact createNewContact(String email, String privatePhoneNumber, String businessPhoneNumber) {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setPrivatePhoneNumber(privatePhoneNumber);
        contact.setBusinessPhoneNumber(businessPhoneNumber);
        return contact;
    }

    private List<String> selectRandomFields() {
        Collections.shuffle(ContactGenerator.CONTACT_FIELDS);
        return ContactGenerator.CONTACT_FIELDS.subList(0, 2);
    }

    private String contactToCsvLine(Contact contact) {
        return String.format("%s,%s,%s",
                contact.getEmail(),
                contact.getPrivatePhoneNumber(),
                contact.getBusinessPhoneNumber());
    }

    private List<Contact> generateContactsFromCsvFile() {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ContactGenerator.CONTACT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Contact contact = csvLineToContact(line);
                contacts.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    private Contact csvLineToContact(String line) {
        String[] data = line.split(",");
        if (data.length == 3) {
            String email = data[0];
            String privatePhoneNumber = data[1];
            String businessPhoneNumber = data[2];

            Contact contact = new Contact();
            contact.setEmail(email);
            contact.setPrivatePhoneNumber(privatePhoneNumber);
            contact.setBusinessPhoneNumber(businessPhoneNumber);
            return contact;
        }
        return null;
    }

}
