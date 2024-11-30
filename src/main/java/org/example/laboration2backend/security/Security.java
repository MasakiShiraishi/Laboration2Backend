package org.example.laboration2backend.security;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.example.laboration2backend.user.AppUserRepository;
import org.example.laboration2backend.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class Security {

    @Bean public AuthenticationProvider authenticationProvider(AppUserRepository appUserRepository) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(appUserRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, ApiKeyAuthService apiKeyAuthService) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
//                .addFilterBefore(new ApiKeyAuthFilter(apiKeyAuthService), UsernamePasswordAuthenticationFilter.class);
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(GET, "/category/**").permitAll()
                                .requestMatchers(POST, "/category").hasRole("ADMIN")
                                .requestMatchers(GET, "/place/**").permitAll()
                                .requestMatchers(GET, "/places/**").hasAnyRole("USER101","USER102", "USER103","USER104","USER105")
                                .requestMatchers(GET, "/test/**").permitAll()
                                .requestMatchers(POST, "/place/**").authenticated()
                                .requestMatchers(DELETE, "/place/**").hasAnyRole("USER101","USER102", "USER103","USER104","USER105")
                                .requestMatchers(PUT, "/place/**").authenticated()
                                .requestMatchers(GET, "/playgrounds/**").permitAll()
                                .requestMatchers(POST, "/playgrounds").permitAll()
                                .requestMatchers(GET, "/user/**").permitAll()
                                .anyRequest().denyAll())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults())
//                .exceptionHandling(exception ->
//                        exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AppUserRepository appUserRepository) {
        return new CustomUserDetailsService(appUserRepository); }
}