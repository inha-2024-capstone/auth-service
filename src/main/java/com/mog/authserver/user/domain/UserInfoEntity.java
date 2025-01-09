package com.mog.authserver.user.domain;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.common.entity.BaseEntity;
import com.mog.authserver.user.domain.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
    private String phoneNumber;

    private String address;

    private String nickName;

    private String imageUrl;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity authEntity;

    public UserInfoEntity(LocalDateTime createTime, LocalDateTime modifiedTime,
                          LocalDateTime deletedTime, State state, Long id, Gender gender, String phoneNumber,
                          String address, String nickName, String imageUrl, AuthEntity authEntity) {
        super(createTime, modifiedTime, deletedTime, state);
        this.id = id;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.authEntity = authEntity;
    }
}
