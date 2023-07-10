package com.ostasz.ads.user.datamodel.entity.baseEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    protected Long id;

    @Column
    protected OffsetDateTime createdAt;

    @Column
    protected OffsetDateTime modifiedAt = OffsetDateTime.now();

    @PrePersist
    void setCreatedAt(){
        if(this.createdAt == null)
            this.setCreatedAt(OffsetDateTime.now());
    }

    @PreUpdate
    void setModifiedAt(){
        this.modifiedAt = OffsetDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
