package org.example.laboration2backend.security;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.example.laboration2backend.user.AppUserRepository;
import org.example.laboration2backend.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class Security {

//    @Bean public AuthenticationProvider authenticationProvider(AppUserRepository appUserRepository) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService(appUserRepository));
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    public Security(JwtUtil jwtUtil, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration; }
//    @Bean
//    @Order(1)
//    public SecurityFilterChain apiFilterChain(HttpSecurity http, ApiKeyAuthService apiKeyAuthService) throws Exception {
//        http
//                .securityMatcher("/api/**")
//                .csrf(AbstractHttpConfigurer::disable)
////                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
////                .addFilterBefore(new ApiKeyAuthFilter(apiKeyAuthService), UsernamePasswordAuthenticationFilter.class);
////                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil));
//        return http.build();
//    }
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    JwtAuthenticationFilter jwtAuthenticationFilter =
//            new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil);
//    jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeRequests(authz -> authz
//                        .requestMatchers("/auth/login").permitAll()
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().permitAll() )
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil));
////        return http.build();
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        http .csrf(csrf -> csrf.disable())
                .authorizeRequests(authz -> authz
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll() )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build(); }

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
                                .requestMatchers("/oauth/token").permitAll()
                                .requestMatchers("/auth/login").permitAll()
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

    @Bean public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); }

//    @Bean
//    public UserDetailsService userDetailsService(AppUserRepository appUserRepository) {
//        return new CustomUserDetailsService(appUserRepository);
//    }

//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return converter; }


}