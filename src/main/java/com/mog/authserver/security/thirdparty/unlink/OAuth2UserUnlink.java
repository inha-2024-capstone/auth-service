package com.mog.authserver.security.thirdparty.unlink;

public interface OAuth2UserUnlink {
    void unlink(String accessToken);
}
