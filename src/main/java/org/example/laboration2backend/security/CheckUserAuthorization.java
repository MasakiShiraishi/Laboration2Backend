package org.example.laboration2backend.security;

import org.example.laboration2backend.entity.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CheckUserAuthorization {

        public void checkUserAuthorization(AppUser appUser) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username;

            if (authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                username = jwt.getClaim("sub");
            } else if (authentication.getPrincipal() instanceof UserDetails) {
                username = ((UserDetails) authentication.getPrincipal()).getUsername();
            } else {
                username = authentication.getPrincipal().toString();
            }

            if (!appUser.getUsername().equals(username)) {
                throw new SecurityException("Unauthorized to update or delete this place");
            }
        }
    }

