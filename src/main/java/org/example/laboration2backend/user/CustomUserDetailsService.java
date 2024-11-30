package org.example.laboration2backend.user;

import org.example.laboration2backend.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<SimpleGrantedAuthority> authorities = Arrays.stream(appUser.getRole().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        return User.builder() .username(appUser.getUsername()) .password(appUser.getPassword()) .authorities(authorities) .build();

    }
}

