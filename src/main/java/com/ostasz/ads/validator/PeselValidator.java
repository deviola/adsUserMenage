package com.ostasz.ads.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PeselValidator {

    public String validate(String pesel) {
        if (pesel.length() != 11) {
            throw new IllegalArgumentException("PESEL length is incorrect: " + pesel);
        }
        if (!pesel.matches("\\d+")) {
            throw new IllegalArgumentException("PESEL must contain only digits: " + pesel);
        }
        return pesel;
    }

}
