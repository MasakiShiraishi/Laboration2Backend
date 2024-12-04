package org.example.laboration2backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
    @NotEmpty
    private String username;

    @NotEmpty
    @Size(min = 6, message = "Minimum Password length in 6 characters")
    private String password;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    public @NotEmpty String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty String username) {
        this.username = username;
    }

    public @NotEmpty @Size(min = 6, message = "Minimum Password length in 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty @Size(min = 6, message = "Minimum Password length in 6 characters") String password) {
        this.password = password;
    }

    public @Size(max = 50) @NotEmpty String getRole() {
        return role;
    }

    public void setRole(@Size(max = 50) @NotEmpty String role) {
        this.role = role;
    }
}
