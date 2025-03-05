package com.mog.authserver.auth.repository;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

    Optional<AuthEntity> findByEmailAndLoginSource(String email, LoginSource loginSource);

    boolean existsByEmailAndLoginSource(String email, LoginSource loginSource);
}
