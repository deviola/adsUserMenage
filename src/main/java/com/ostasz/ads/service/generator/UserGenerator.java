package com.ostasz.ads.service.generator;

import com.github.javafaker.Faker;
import com.ostasz.ads.datamodel.entity.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserGenerator {

    private final PeselGenerator peselGenerator;

    public UserGenerator(PeselGenerator peselGenerator) {
        this.peselGenerator = peselGenerator;
    }

    public void generateUsersToFile(String filePath, Integer usersAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Faker faker = new Faker(new Locale("pl"));

            for (int i = 0; i < usersAmount; i++) {
                String firstName = generateRandomFirstName(faker);
                String lastName = generateRandomLastName(faker);
                String pesel = generateRandomPesel(faker);
                String line = String.format("%s,%s,%s", firstName, lastName, pesel);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> generateUsersFromCsvFile(String filePath) {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String firstName = data[0];
                    String lastName = data[1];
                    String pesel = data[2];
                    User user = new User(firstName, lastName, pesel, null);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    private String generateRandomFirstName(Faker faker) {
        return faker.name().firstName();
    }

    private String generateRandomLastName(Faker faker) {
        return faker.name().lastName();
    }

    private String generateRandomPesel(Faker faker) {
       return peselGenerator.generateRandomPesel(faker);
    }


}
