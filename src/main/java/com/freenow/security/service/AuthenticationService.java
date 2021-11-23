package com.freenow.security.service;

import com.freenow.security.datatransferobject.AuthenticationRequestDTO;
import com.freenow.security.datatransferobject.AuthenticationResponseDTO;
import com.freenow.security.exception.InvalidCredentialException;

public interface AuthenticationService {
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws InvalidCredentialException;
}
