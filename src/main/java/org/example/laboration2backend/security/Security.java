package org.example.laboration2backend.security;

import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
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

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class Security {
    private final Environment env;

    public Security(Environment env) {
        this.env = env;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, ApiKeyAuthService apiKeyAuthService) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
//                        authorize.anyRequest().authenticated())
//                .addFilterBefore(new ApiKeyAuthFilter(apiKeyAuthService), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
                        authorize.requestMatchers("/api/playgrounds/**").permitAll()
                                .anyRequest().authenticated());

        if (!isTestProfileActive()) {
            http.addFilterBefore(new ApiKeyAuthFilter(apiKeyAuthService), UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    private boolean isTestProfileActive() {
        return Arrays.asList(env.getActiveProfiles()).contains("test");
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
                                .requestMatchers(GET, "/playgrounds/index.html", "/js/**").permitAll()
                                .requestMatchers(
//                                        "/v1/api/**",
//                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                ).permitAll()
                                .requestMatchers("/error").permitAll()
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