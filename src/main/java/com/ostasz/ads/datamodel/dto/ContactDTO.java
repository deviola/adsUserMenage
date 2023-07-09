package com.ostasz.ads.datamodel.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private String email;
    private AddressDTO homeAddress;
    private AddressDTO registeredAddress;
    private String privatePhoneNumber;
    private String BusinessPhoneNumber;
}
