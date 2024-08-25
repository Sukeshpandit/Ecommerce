package com.app.dev.JsonWebTokenServices;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.dev.Model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private static final String secretkey = "76259a66b3729a09c053f67de9080f41abe392e4622a4891d6561ae9e00c0cc1d6a0874c6e12f7e5e456d1842da97d3112b5177c264fc8b777e849dbaca99b72c4df479a1ec22989856aee44096427fed0370c8768c6826f4b38cb05161cc081078ae3944d71310b19c50676fbfa712d79c8a02f0959c06097a58e2114485189ce2897795b5370ff490534d2f4389b2658fb3c0f0691b049884e54fb441e9bad5c43c7069d6bf3cf5459996742216540e5deb54166165001eba080501672969449d0fbbb94c5e1333cdacd14ab955b37110a51372d206e02b8fb4203b8e9aa57537c502472e0525effc6f454aa7ad606ac168d311c323c5de9bea2ee3183ec09";
	private long jwtExpiration = 1000*60*30;

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(Users users) {
		return generateToken(new HashMap<>(), users);
	}

	public String generateToken(Map<String, String> extractClaims, Users userDetails) {
		
		System.out.println(userDetails.getUsername());
		return Jwts.builder().setSubject(userDetails.getUsername()).setClaims(extractClaims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignkey(), SignatureAlgorithm.HS512).compact();
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignkey()).build().parseClaimsJws(token).getBody();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Key getSignkey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretkey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
