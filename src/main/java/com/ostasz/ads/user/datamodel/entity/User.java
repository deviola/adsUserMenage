package com.ostasz.ads.user.datamodel.entity;

import com.ostasz.ads.user.datamodel.entity.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Contact contact;
}
