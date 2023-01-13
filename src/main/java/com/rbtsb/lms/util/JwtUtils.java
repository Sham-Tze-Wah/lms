package com.rbtsb.lms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

//https://medium.com/innoventes/spring-boot-with-jwt-7970e5be4540

@Component
@Slf4j
public class JwtUtils {
    private String jwtSigningKey = "secret";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean hasClaim(String token, String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
//        log.info(claims.toString());
//        log.info(userDetails.getAuthorities().toString());
        return createToken(claims, userDetails);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> claims){
        return createToken(claims, userDetails);
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails){
        try{
//            log.info(userDetails.getAuthorities().toString());
            return Jwts.builder().setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .claim("authorities", userDetails.getAuthorities())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                    .signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
        }
        catch(Exception ex){
            return "signing failed";
        }
    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

