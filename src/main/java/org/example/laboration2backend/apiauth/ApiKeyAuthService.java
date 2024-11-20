package org.example.laboration2backend.apiauth;


import org.example.laboration2backend.entity.ApiKey;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class ApiKeyAuthService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuthService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }
    public Optional<String> isValidApiKey(String apiKey) {
        if (apiKey != null && !apiKey.isEmpty()) {
            var key = apiKeyRepository.findByApiKey(apiKey);
            if ( key.isPresent() && key.get().getValidUntil().isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC))) {
                return Optional.of(key.get().getName());
            }
        }
        return Optional.empty();
    }
    @PostFilter("filterObject.name == authentication.name")
    public List<ApiKey> getMyApiKeys() {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return apiKeyRepository.findApiKeysByName(name);
    }
}
