package com.mog.authserver.user.domain;

import com.mog.authserver.common.entity.BaseEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInfoEntity extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private String email;
    @Column(updatable = false)
    private String username;
    @Column(nullable = false)
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(min=13, max=13) @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
    private String phoneNumber;

    @Setter
    private String address;

    @Setter
    private String nickName;

    @Setter
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private LoginSource loginSource;

}
