package com.ostasz.ads.service.generator;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class PeselGenerator {

    public String generateRandomPesel(Faker faker) {
        StringBuilder peselBuilder = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int randomDigit = faker.random().nextInt(10);
            peselBuilder.append(randomDigit);
        }

        String partialPesel = peselBuilder.toString();
        partialPesel += "00";
        int controlSum = calculateControlSum(partialPesel);
        int controlDigit = (10 - (controlSum % 10)) % 10;
        peselBuilder.append(controlDigit);

        return peselBuilder.toString();
    }

    private int calculateControlSum(String pesel) {
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1};
        int sum = 0;

        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(pesel.charAt(i)) * weights[i];
        }

        int controlSum = 10 - (sum % 10);
        return controlSum % 10;
    }
}
