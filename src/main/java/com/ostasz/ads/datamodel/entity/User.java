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
@Table(name = "`Users`")
public class User extends BaseEntity {

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String pesel;

    @ManyToOne
    @JoinColumn
    private Contact contact;
}
