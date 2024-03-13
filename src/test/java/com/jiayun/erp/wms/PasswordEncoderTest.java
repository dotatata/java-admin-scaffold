package com.jiayun.erp.wms;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.crypto.KeyGenerator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class PasswordEncoderTest {

    @Test
    public void pbkdf2PasswordEncoderTest(){
        BytesKeyGenerator saltGenerator = KeyGenerators.secureRandom(4);
        byte[] salt = saltGenerator.generateKey();
        System.out.println("BytesKeyGenerator generate key : " + Arrays.toString(salt));
        String saltToString = new String(Hex.encode(salt));
        System.out.println("BytesKeyGenerator generate key to string : " + saltToString);

        StringKeyGenerator stringKeyGenerator = KeyGenerators.string();
        String stringSalt = stringKeyGenerator.generateKey();
        System.out.println("StringKeyGenerator generate key : " + stringSalt);

        // 创建一个Pbkdf2PasswordEncoder实例
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        // 用户设定的密码
        String rawPassword = "123";
        // 使用Pbkdf2PasswordEncoder加密密码
        String encodedPassword = passwordEncoder.encode(rawPassword);
        // 输出加密后的密码
        System.out.println("Encoded Password: " + encodedPassword);

        // match
        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("encodedPassword match rawPassword is " + isMatch);
    }

    @Test
    public void bCryptPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 用户设定的密码
        String rawPassword = "ooxxooxx";

        System.out.println(System.currentTimeMillis());
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        System.out.println(System.currentTimeMillis());
        // 输出加密后的密码
        System.out.println("Encoded Password: " + encodedPassword);

        boolean isMatch = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("encodedPassword match rawPassword is " + isMatch);

    }
}