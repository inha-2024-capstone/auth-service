package com.mog.authserver.user.repository;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    @Transactional(readOnly = true)
    Optional<UserInfoEntity> findByEmailAndLoginSource(String email, LoginSource loginSource);

    Boolean existsByEmailAndLoginSource(String email, LoginSource loginSource);

}
