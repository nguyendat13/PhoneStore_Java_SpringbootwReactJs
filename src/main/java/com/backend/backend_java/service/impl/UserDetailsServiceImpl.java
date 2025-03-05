package com.backend.backend_java.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.backend_java.config.UserInfoConfig;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);

        return user.map(UserInfoConfig::new)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }
}
