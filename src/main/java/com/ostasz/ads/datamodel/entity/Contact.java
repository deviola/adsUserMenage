package com.ostasz.ads.datamodel.entity;

import com.ostasz.ads.datamodel.entity.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "`Contacts`")
public class Contact extends BaseEntity {

    @Column
    private String email;

    @ManyToOne
    @JoinColumn
    private Address homeAddress;

    @ManyToOne
    @JoinColumn
    private Address registeredAddress;

    @Column
    private String privatePhoneNumber;

    @Column
    private String businessPhoneNumber;

}
