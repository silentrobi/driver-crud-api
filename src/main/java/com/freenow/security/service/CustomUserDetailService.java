package com.freenow.security.service;

import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.DriverOfflineException;
import com.freenow.exception.EntityNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    /**
     * load user by username.
     *
     * @param username
     * @throws UsernameNotFoundException  if no driver with the given id was found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("admin", "12345", new ArrayList<>());
    }
}
