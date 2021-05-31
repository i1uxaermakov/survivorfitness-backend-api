package com.changeplusplus.survivorfitness.backendapi.security;

import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePwdUserInfoAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private UserDTO userDTO;

    public UsernamePwdUserInfoAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UsernamePwdUserInfoAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
