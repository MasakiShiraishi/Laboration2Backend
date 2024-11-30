package org.example.laboration2backend.user;

import org.example.laboration2backend.entity.AppUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends ListCrudRepository<AppUser, Integer> {
    AppUser findByUsername(String username);
}

