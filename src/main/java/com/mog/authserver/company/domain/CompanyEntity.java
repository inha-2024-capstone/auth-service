package com.mog.authserver.company.domain;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.common.entity.BaseEntity;
import com.mog.authserver.company.validator.ValidPhoneNumber;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id")
    private AuthEntity authEntity;

    public CompanyEntity(LocalDateTime createTime, LocalDateTime modifiedTime,
                         LocalDateTime deletedTime, State state, Long id, String phoneNumber, String address,
                         String description, String shortDescription, String imageUrl, AuthEntity authEntity) {
        super(createTime, modifiedTime, deletedTime, state);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.authEntity = authEntity;
    }
}
