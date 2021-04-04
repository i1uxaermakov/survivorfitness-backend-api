package com.changeplusplus.survivorfitness.backendapi.security;

import com.changeplusplus.survivorfitness.backendapi.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CurrentUserDetails implements UserDetails {
    private final String ROLE_PREFIX = "ROLE_";

    private String username;
    private String password;
    private List<UserRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        for(UserRole userRole: roles) {
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX + userRole.getName()));
        }
        return list;
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
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
