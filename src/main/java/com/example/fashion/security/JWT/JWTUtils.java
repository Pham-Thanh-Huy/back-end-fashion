package com.example.fashion.security.JWT;

import com.example.fashion.entity.Authorize;
import com.example.fashion.entity.User;
import com.example.fashion.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    private String SECRET;

    private static final long EXPIRATION_TIME = 8760*60*60*1000; // 1 year

    public JWTUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        User user = userRepository.getUserByUsername(username);
        for(Authorize authorize : user.getAuthorizeList()){
            if(authorize.getAuthorizeName().equals("ADMIN")){
                claims.put("isAdmin", true);
            }
            if(authorize.getAuthorizeName().equals("USER")){
                claims.put("isUser", true);
            }
            if(authorize.getAuthorizeName().equals("STAFF")){
                claims.put("isStaff", true);
            }
        }

        claims.put("userId", user.getUserId());

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
