package com.ostasz.ads.user.datamodel.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    private String email;
    private String homeAddress;
    private String registeredAddress;
    private String privatePhoneNumber;
    private String businessPhoneNumber;
}
