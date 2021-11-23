package com.freenow.controller;


import com.freenow.security.datatransferobject.AuthenticationRequestDTO;
import com.freenow.security.datatransferobject.AuthenticationResponseDTO;
import com.freenow.security.exception.InvalidCredentialException;
import com.freenow.security.service.AuthenticationService;
import com.freenow.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponseDTO getAuthToken(@RequestBody AuthenticationRequestDTO dto) throws InvalidCredentialException {
        return authenticationService.authenticate(dto);
    }
}
