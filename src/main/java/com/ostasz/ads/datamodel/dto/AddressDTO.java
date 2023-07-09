package com.ostasz.ads.datamodel.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String streetName;
    private String streetNumber;
    private String houseNumber;
    private String country;
    private String postalCode;
}
