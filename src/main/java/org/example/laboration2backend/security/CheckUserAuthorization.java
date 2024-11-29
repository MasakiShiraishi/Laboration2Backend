package org.example.laboration2backend.security;

import org.example.laboration2backend.entity.Place;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CheckUserAuthorization {

    public void checkUserAuthorization(Place place) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername;
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            currentUsername = principal.toString();
        }

        String placeOwner = "user" + place.getUserId();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        log.debug("Place owner: {}, Current user: {}, Is admin: {}", placeOwner, currentUsername, isAdmin);

        if (!isAdmin && !placeOwner.equals(currentUsername)) {
            throw new SecurityException("Unauthorized to update or delete this place");
        }
    }
}
