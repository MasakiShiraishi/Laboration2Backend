package org.example.laboration2backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class ApiKeyAuthFilter extends GenericFilterBean {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private final ApiKeyAuthService apiKeyAuthService;

    public ApiKeyAuthFilter(ApiKeyAuthService apiKeyAuthService) {
        this.apiKeyAuthService = apiKeyAuthService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Authentication authentication = getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            writer.print(exp.getMessage());
            writer.flush();
            writer.close();
        }

        chain.doFilter(request, response);
    }


    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        var name = apiKeyAuthService.isValidApiKey(apiKey);
        if (name.isEmpty()) {
            throw new BadCredentialsException("Invalid API Key");
        }
        return new ApiKeyAuthentication(name.get(), AuthorityUtils.createAuthorityList("read:test","read:test"));
    }

}


