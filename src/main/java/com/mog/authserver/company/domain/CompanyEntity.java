package com.mog.authserver.company.domain;

import com.mog.authserver.common.entity.BaseEntity;
import com.mog.authserver.company.validator.ValidPhoneNumber;
import com.mog.authserver.user.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CompanyEntity extends BaseEntity {
    @Column(updatable = false)
    private String companyName;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ValidPhoneNumber
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String shortDescription;

    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private Role role;

    public CompanyEntity(Long id, LocalDateTime createTime, LocalDateTime modifiedTime,
                         LocalDateTime deletedTime, State state, String companyName, String email,
                         String password, String phoneNumber, String address, String description,
                         String shortDescription,
                         String imageUrl, Role role) {
        super(id, createTime, modifiedTime, deletedTime, state);
        this.companyName = companyName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.role = role;
    }
}
