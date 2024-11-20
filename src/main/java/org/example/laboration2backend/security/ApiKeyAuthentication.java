package org.example.laboration2backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}
/*
In the ApiKeyAuthentication class, the getCredentials method is supposed to return the credentials associated with
the authentication request. However, in this specific implementation, it returns null.
This is because the authentication is based on an API key, and the API key itself is considered the principal
(i.e., the identity of the authenticated entity). Therefore, there are no additional credentials to return.

In other authentication mechanisms, such as username/password authentication, the getCredentials method would typically
return the password or other secret used for authentication.
But in this case, since the API key is the primary means of authentication, the method returns null.
 */
