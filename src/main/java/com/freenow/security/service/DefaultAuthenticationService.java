package com.freenow.security.service;

import com.freenow.security.datatransferobject.AuthenticationRequestDTO;
import com.freenow.security.datatransferobject.AuthenticationResponseDTO;
import com.freenow.security.exception.InvalidCredentialException;
import com.freenow.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Autowired
    public DefaultAuthenticationService(final AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws InvalidCredentialException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getUsername(),
                            authenticationRequestDTO.getPassword()
                    ));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialException("Invalid credential");
        }

        final UserDetails userDetails = userDetailsService.
                loadUserByUsername(authenticationRequestDTO.getUsername());
        final String token = JwtUtil.generateToken(userDetails);

        return new AuthenticationResponseDTO(token);
    }
}
