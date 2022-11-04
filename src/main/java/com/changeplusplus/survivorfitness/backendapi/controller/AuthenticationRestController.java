package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.AuthenticationRequest;
import com.changeplusplus.survivorfitness.backendapi.dto.AuthenticationResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.UserListResponse;
import com.changeplusplus.survivorfitness.backendapi.security.UsernamePwdUserInfoAuthenticationToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
@Api(tags = "Authentication Controller", description = "Endpoints for obtaining a JWT token required for authentication.")
public class AuthenticationRestController {

    @Value("${survivorfitness-backend.security.jwt.secret-key}")
    private String secretKey;

    @Value("${survivorfitness-backend.security.jwt.subject}")
    private String subject;

    @Value("${survivorfitness-backend.security.jwt.issuer}")
    private String issuer;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * API operation to request a user authentication
     * @param authenticationRequest - a request to authenticate that includes a username and a password
     * @return JWT token and user object if the authentication request is successful
     * @throws Exception - throws an exception if the authentication request is invalid (username or password is invalid)
     */
    @PostMapping("/authenticate")
    @ApiOperation(value = "Finds a user with the provided username and responds with " +
            "JWT-token and info about the user if authentication is successful.",
            notes = "The endpoint is open to authenticated and not authenticated users.",
            response = AuthenticationResponse.class)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        UsernamePwdUserInfoAuthenticationToken authentication = new UsernamePwdUserInfoAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        try {
            authentication = (UsernamePwdUserInfoAuthenticationToken) authenticationManager.authenticate(authentication);
        }
        catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect username or password");
        }
        catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username not found!");
        }
        catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account disabled, cannot log in!");
        }

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder().setIssuer(issuer).setSubject(subject)
                .claim("username", authentication.getName())
                .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1000 * 60 * 60 * 24)) //24 hours expiration
                .signWith(key, SignatureAlgorithm.HS256).compact();

        return ResponseEntity.ok(new AuthenticationResponse(jwt, authentication.getUserDTO()));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

}
