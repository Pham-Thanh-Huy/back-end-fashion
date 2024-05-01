package com.example.backendfruitable.security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    @Value("${jwt.secretKey}")
    private String SECRET;

    private static final long EXPIRATION_TIME = 30*60*1000;

    private String createToken(Map<String, Object> claims, String username){
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
    }


    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
       return  createToken(claims, username);
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .build().parseClaimsJws(token)
                .getBody();
    }


    // lấy username
    public String getUsernameClaims(String token){
        return parseClaims(token).getSubject();
    }

    // lấy thời gian jwt
    public Date getExpirationClaims(String token){
        return parseClaims(token).getExpiration();
    }

    // kiểm tra thời gian hết hạn jwt
    public Boolean isTokenExpired(String token){
        return getExpirationClaims(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameClaims(token);
        if(!userDetails.getUsername().equals(username)){
            return false;
        }
        return true;
    }
}
