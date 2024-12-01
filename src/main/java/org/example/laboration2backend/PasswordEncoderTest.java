package org.example.laboration2backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String rawPassword = "password";
    // 生パスワード // 各ユーザーのエンコードされたパスワードを出
        System.out.println("user101: " + passwordEncoder.encode(rawPassword));
        System.out.println("user102: " + passwordEncoder.encode(rawPassword));
        System.out.println("user103: " + passwordEncoder.encode(rawPassword));
        System.out.println("user104: " + passwordEncoder.encode(rawPassword));
        System.out.println("user105: " + passwordEncoder.encode(rawPassword));
        System.out.println("admin: " + passwordEncoder.encode(rawPassword)); }
    }
