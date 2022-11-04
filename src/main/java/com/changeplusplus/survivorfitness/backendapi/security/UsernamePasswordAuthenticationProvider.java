package com.changeplusplus.survivorfitness.backendapi.security;

import com.changeplusplus.survivorfitness.backendapi.application.DemoData;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final String ROLE_PREFIX = "ROLE_";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserManagementService userManagementService;

    private Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName().toLowerCase();
        String pwd = authentication.getCredentials().toString();

        UserDTO userDTO = userManagementService.getUserInfoByEmail(username);

        if(userDTO==null) {
            logger.info("Received a Login Request for an account that doesn't exist");
            throw new UsernameNotFoundException("Username not found!");
        }

        if(!userDTO.getIsEnabled()) {
            logger.info("Received a Login Request for a disabled account");
            throw new DisabledException("Account disabled!");
        }

        if (!passwordEncoder.matches(pwd, userDTO.getPassword())) {
            logger.info("Received a Login Request for an account and the password didn't match");
            throw new BadCredentialsException("Invalid password!");
        }

        List<GrantedAuthority> listOfRoles = new ArrayList<GrantedAuthority>();

        if(Objects.nonNull(userDTO.getRoles())) {
            for (String userRole : userDTO.getRoles()) {
                listOfRoles.add(new SimpleGrantedAuthority(ROLE_PREFIX + userRole));
            }
        }

        UsernamePwdUserInfoAuthenticationToken authToken = new UsernamePwdUserInfoAuthenticationToken(username, pwd, listOfRoles);
        authToken.setUserDTO(userDTO);
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UsernamePwdUserInfoAuthenticationToken.class);
    }
}
