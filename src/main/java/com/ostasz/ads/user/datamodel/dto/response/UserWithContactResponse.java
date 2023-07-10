package com.ostasz.ads.user.datamodel.dto.response;

import com.ostasz.ads.user.datamodel.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserWithContactResponse extends UserResponse {

    private String email;
    private String homeAddress;
    private String registeredAddress;
    private String privatePhoneNumber;
    private String businessPhoneNumber;

    public UserWithContactResponse(User user) {
        this.email = user.getContact().getEmail();
        this.homeAddress = user.getContact().getHomeAddress();
        this.registeredAddress = user.getContact().getRegisteredAddress();
        this.privatePhoneNumber = user.getContact().getPrivatePhoneNumber();
        this.businessPhoneNumber = user.getContact().getBusinessPhoneNumber();
    }
}
