package com.opencode.practice.models;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Access.DEVELOPERS_READ)),
    ADMIN(Set.of(Access.DEVELOPERS_READ, Access.DEVELOPERS_WRITE));

    private final Set<Access> accesses;

    Role(Set<Access> accesses) {
        this.accesses = accesses;
    }

    public Set<Access> getAccess() {
        return accesses;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getAccess().stream()
                .map(access -> new SimpleGrantedAuthority(access.getAccess()))
                .collect(Collectors.toSet());
    }
}
