package org.example.laboration2backend.controller;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.validation.Valid;
import org.example.laboration2backend.dto.LoginDto;
import org.example.laboration2backend.dto.RegisterDto;
import org.example.laboration2backend.entity.AppUser;
import org.example.laboration2backend.user.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;


@RestController
@RequestMapping("/user")
public class AuthController {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth) {
         var response = new HashMap<String, Object>();
         response.put("username", auth.getName());
         response.put("Authorities", auth.getAuthorities());

         var appUser = appUserRepository.findByUsername(auth.getName());
         response.put("user", appUser);
         return ResponseEntity.ok(response);
         }

    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @Valid @RequestBody RegisterDto registerDto,
            BindingResult result){
        if(result.hasErrors()){
            var errosList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();
            for(int i= 0; i<errosList.size(); i++){
                var error = (FieldError) errosList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        var bCryptEncoder = new BCryptPasswordEncoder();
        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setRole(registerDto.getRole());
        appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
        try{
            var otherUser = appUserRepository.findByUsername(registerDto.getUsername());
            if(otherUser != null){
                return ResponseEntity.badRequest().body("Username already exists");
            }
            appUserRepository.save(appUser);
            String jwtToken = createJwtToken(appUser);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", appUser);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            System.out.println("There is an Exception :");
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error"); }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @Valid @RequestBody LoginDto loginDto
            , BindingResult result) {
        if (result.hasErrors()) {
            var errosList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errosList.size(); i++) {
                var error = (FieldError) errosList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            AppUser appUser = appUserRepository.findByUsername(loginDto.getUsername());

            String jwtToken = createJwtToken(appUser);
            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", appUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("There is an Exception :");
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Bad username or password");
    }

    private String createJwtToken(AppUser appUser){
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .issuedAt(now.plusSeconds(3600))
                .subject(appUser.getUsername())
                .claim("role", appUser.getRole())
                .build();

        var encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),claims);

        return encoder.encode(params).getTokenValue();

    }
}
