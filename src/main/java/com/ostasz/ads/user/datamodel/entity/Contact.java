package com.ostasz.ads.user.datamodel.entity;

import com.ostasz.ads.user.datamodel.entity.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "`Contacts`")
public class Contact extends BaseEntity {

    @Column
    private String email;

    @Column
    private String homeAddress;

    @Column
    private String registeredAddress;

    @Column
    private String privatePhoneNumber;

    @Column
    private String businessPhoneNumber;

}
