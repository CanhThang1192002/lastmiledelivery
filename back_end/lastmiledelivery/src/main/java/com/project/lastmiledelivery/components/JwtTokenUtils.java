package com.project.lastmiledelivery.components;

import com.project.lastmiledelivery.exceptions.InvalidParamException;
import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.repositories.CustomerRepository;
import com.project.lastmiledelivery.repositories.ShipperRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int EXPIRATION;

    @Value("${jwt.refreshable-expiration}")
    private int REFRESHABLE_EXPIRATION;

    @Value("${jwt.secretKey}")
    private String SECRETKEY;

    // sinh khoa bi mat
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    // sinh token customer
    public String generateTokenCustomer(Customer customer) throws Exception{
        Map<String, Object> claims = new HashMap<>();
        String role = customer.getRole().getRoleName();
        claims.put("role", role);
//        claims.put("name", customer.getFullName());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(customer.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e) {
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
        }
    }
    // sinh token shipper
    public String generateTokenShipper(com.project.lastmiledelivery.models.Shipper shipper) throws Exception{
        Map<String, Object> claims = new HashMap<>();
        String role = shipper.getRole().getRoleName();
        claims.put("role", role);
//        claims.put("name", shipper.getFullName());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(shipper.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e) {
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRETKEY);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI="));
        return Keys.hmacShaKeyFor(bytes);
    }

    // lay thong tin tu token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e; // Xử lý ngoại lệ token hết hạn
        } catch (JwtException e) {
            throw e;
        }
    }

    // lay thong tin tu token
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // kiem tra token co het han hay chua
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    // lay so dien thoai tu token
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//    public String extractName(String token) {
//        return extractClaim(token, claims -> (String) claims.get("name"));
//    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    // kiem tra token
    public boolean validateToken(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }


}

