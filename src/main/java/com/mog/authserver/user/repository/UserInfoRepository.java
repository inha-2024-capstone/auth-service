package com.mog.authserver.user.repository;

import com.mog.authserver.user.domain.UserInfoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {

    Boolean existsByNickName(String nickname);

    Optional<UserInfoEntity> findByAuthEntity_Id(Long authEntity_id);
}
