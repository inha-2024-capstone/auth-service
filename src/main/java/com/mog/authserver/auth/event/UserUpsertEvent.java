package com.mog.authserver.auth.event;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Role;
import java.util.UUID;

public record UserUpsertEvent(
        Long id,
        String username,
        String email,
        String imageUrl,
        Role role,
        String eventId
) {
    public static UserUpsertEvent from(UserInfoEntity userInfoEntity) {
        return new UserUpsertEvent(
                userInfoEntity.getId(),
                userInfoEntity.getAuthEntity().getUsername(),
                userInfoEntity.getAuthEntity().getEmail(),
                userInfoEntity.getImageUrl(),
                userInfoEntity.getAuthEntity().getRole(),
                UUID.randomUUID().toString());
    }

    public static UserUpsertEvent from(CompanyEntity companyEntity) {
        return new UserUpsertEvent(
                companyEntity.getId(),
                companyEntity.getAuthEntity().getUsername(),
                companyEntity.getAuthEntity().getEmail(),
                companyEntity.getImageUrl(),
                companyEntity.getAuthEntity().getRole(),
                UUID.randomUUID().toString());
    }
}
