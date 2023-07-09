package com.ostasz.ads.datamodel.entity.baseEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public class BaseIdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    protected Long id;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
