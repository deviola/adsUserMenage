package com.ostasz.ads.datamodel.entity.baseEntity;

import com.ostasz.ads.datamodel.entity.baseEntity.BaseIdEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity extends BaseIdEntity {

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

}
