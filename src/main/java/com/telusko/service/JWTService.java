package com.telusko.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JWTService {
	
	
	private String secretKey;
	
	public JWTService() {
		
		secretKey=generateSecretKey();
	}
	
	
	public String generateSecretKey() {
		
		try {
			
			KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey=keyGen.generateKey();
			System.out.println("Secret Key :"+ secretKey.toString());
			return Base64.getEncoder().encodeToString(secretKey.getEncoded());
		}
		catch(NoSuchAlgorithmException e) {
			
			throw new RuntimeException("Error generating secret key");
		}
	}
	
	

	public String generateToken(String username) {
		
		Map<String, Object> claims=new HashMap<>();
		
		
		//payload + Key(signature)
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+ 1000*60*3))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	
	}

	
	private Key getKey() {
		
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		// TODO Auto-generated method stub
		
		return extractClaim(token,Claims::getSubject);
	}
	
	
	
	private <T> T extractClaim(String token,Function<Claims,T> claimResolver) {
		System.out.println("Token in extract claim "+ token);
		final Claims claims=extractAllClaims(token);
		System.out.println("Claims "+ claims);
		return claimResolver.apply(claims);
	}
	
	
	


	private Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		System.out.println("Token is extract all claim "+ token);
		
		return Jwts.parser().setSigningKey(getKey())
				.build().parseClaimsJws(token).getBody();
				
	}


	public boolean validateToken(String token, UserDetails userDetails) {
		
		System.out.println("Token in validate token"+token);
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && isTokenExpired(token) );
	}


	private boolean isTokenExpired(String token) {
		
		System.out.println("Checking if token is expired or not.");
		return extractExpiration(token).before(new Date());
	}


	private Date extractExpiration(String token) {
		
		return extractClaim(token,Claims::getExpiration);
	}
}
