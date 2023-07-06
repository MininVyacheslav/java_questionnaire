package com.opencode.practice.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.List;

@Data
@MappedSuperclass
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    private String username;
    private String password;

    @Transient
    private List<SimpleGrantedAuthority> authorities;

    @Transient
    private boolean isActive;

    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getRole().getAuthorities()
        );
    }
}
