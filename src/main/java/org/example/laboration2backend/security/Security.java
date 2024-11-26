package org.example.laboration2backend.security;

import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, ApiKeyAuthService apiKeyAuthService) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .addFilterBefore(new ApiKeyAuthFilter(apiKeyAuthService), UsernamePasswordAuthenticationFilter.class);
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
                                .requestMatchers(DELETE, "/place/**").hasRole("ADMIN")
                                .requestMatchers(PUT, "/place/**").authenticated()
                                .anyRequest().denyAll())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        PasswordEncoder encoder = passwordEncoder();
        UserDetails user101 = User.builder()
                .username("user101")
                .password(encoder.encode("password"))
                .roles("USER101")
                .build();
        UserDetails user102 = User.builder()
                .username("user102")
                .password(encoder.encode("password"))
                .roles("USER102")
                .build();

        UserDetails user103 = User.builder()
                .username("user103")
                .password(encoder.encode("password"))
                .roles("USER103")
                .build();

        UserDetails user104 = User.builder()
                .username("user104")
                .password(encoder.encode("password"))
                .roles("USER104")
                .build();

        UserDetails user105 = User.builder()
                .username("user105")
                .password(encoder.encode("password"))
                .roles("USER105")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("password"))
                .roles("USER101","USER102", "USER103","USER104","USER105", "ADMIN")
                .build();
        System.out.println(encoder.encode("password"));
        return new InMemoryUserDetailsManager(user101,user102, user103, user104, user105, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}