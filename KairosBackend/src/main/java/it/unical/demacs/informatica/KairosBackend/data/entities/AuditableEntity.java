package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

//Used for auditing. Extended by all the entities.
@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity {
    @CreatedBy
    @Column(name = "created_by", columnDefinition="uuid", updatable = false, nullable=false, length = 36)
    protected UUID createdBy;

    //creationDate can be used for both auditing and business logic instead
    @CreatedDate
    @Column(name = "creation_date", nullable=false, updatable = false)
    protected LocalDateTime creationDate;

    //still useful (maybe ADMIN can modify the wishlist)
    @LastModifiedBy
    @Column(name = "last_modified_by", nullable=false)
    protected UUID lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    protected LocalDateTime lastModifiedDate;
}
