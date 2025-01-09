package com.mog.authserver.auth.domain;

import com.mog.authserver.common.entity.BaseEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AuthEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private LoginSource loginSource;

    public AuthEntity(LocalDateTime createTime, LocalDateTime modifiedTime,
                      LocalDateTime deletedTime, State state, Long id, String email, String username,
                      String password, Role role, LoginSource loginSource) {
        super(createTime, modifiedTime, deletedTime, state);
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.loginSource = loginSource;
    }
}
