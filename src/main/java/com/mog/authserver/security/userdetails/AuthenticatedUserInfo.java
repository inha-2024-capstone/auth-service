package com.mog.authserver.security.userdetails;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record AuthenticatedUserInfo(
        Long id,
        String email,
        String name, // name 또는 nickname이 될 수 있음
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}

