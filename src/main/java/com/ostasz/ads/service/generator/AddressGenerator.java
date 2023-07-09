package com.ostasz.ads.service.generator;

import com.github.javafaker.Faker;
import com.ostasz.ads.datamodel.entity.Address;
import com.ostasz.ads.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class AddressGenerator {

    private static final String ADDRESS_FILE_PATH = "address.csv";
    private static final List<String> ADDRESS_FIELDS = Arrays.asList(
            "streetName",
            "streetNumber",
            "houseNumber",
            "country",
            "postalCode"
    );

    private final AddressRepository addressRepository;

    public AddressGenerator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> generateAndSaveAddresses(int amount) {
        generateAddressesToFile(amount);
        List<Address> addresses = generateAddressesFromCsvFile();
        return addressRepository.saveAll(addresses);
    }

    private void generateAddressesToFile(int amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADDRESS_FILE_PATH))) {
            Faker faker = new Faker(new Locale("pl"));

            for (int i = 0; i < amount; i++) {
                Address address = generateRandomAddress(faker);
                String line = addressToCsvLine(address);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Address generateRandomAddress(Faker faker) {
        String streetName = faker.address().streetName();
        String streetNumber = faker.address().buildingNumber();
        String houseNumber = faker.address().secondaryAddress();
        String country = faker.address().country();
        String postalCode = faker.address().zipCode();

        return Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .houseNumber(houseNumber)
                .country(country)
                .postalCode(postalCode)
                .build();
    }

    private List<Address> generateAddressesFromCsvFile() {
        List<Address> addresses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ADDRESS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == ADDRESS_FIELDS.size()) {
                    Address address = mapCsvLineToAddress(data);
                    addresses.add(address);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses;
    }

    private Address mapCsvLineToAddress(String[] data) {
        String streetName = data[0];
        String streetNumber = data[1];
        String houseNumber = data[2];
        String country = data[3];
        String postalCode = data[4];

        return Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .houseNumber(houseNumber)
                .country(country)
                .postalCode(postalCode)
                .build();
    }

    private String addressToCsvLine(Address address) {
        return String.format("%s,%s,%s,%s,%s",
                address.getStreetName(),
                address.getStreetNumber(),
                address.getHouseNumber(),
                address.getCountry(),
                address.getPostalCode());
    }
}
