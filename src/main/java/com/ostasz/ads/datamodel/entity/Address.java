package com.ostasz.ads.datamodel.entity;

import com.ostasz.ads.datamodel.entity.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(name = "`Addresses`")
public class Address extends BaseEntity {

    @Column
    private String streetName;

    @Column
    private String streetNumber;

    @Column
    private String houseNumber;

    @Column
    private String country;

    @Column
    private String postalCode;
}