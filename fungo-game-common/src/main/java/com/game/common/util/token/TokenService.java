package com.game.common.util.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.common.framework.MyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Service
public class TokenService implements ITokenService{
	@Autowired
	private MyProperties myProperties;
	/**
	 * 由字符串生成加密key
	 *
	 * @return
	 */
	public  SecretKey generalKey() {
		byte[] encodedKey = Base64Utils.decodeFromString(myProperties.getSecretKey());
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		return key;
	}

	/**
	 * 创建jwt
	 *
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public  String createJWT(String id, String subject, long ttlMillis) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		SecretKey key = generalKey();
		JwtBuilder builder = Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + ttlMillis)).setId(id).setIssuedAt(new Date()).setSubject(subject).signWith(signatureAlgorithm, key);
		return builder.compact();
	}

	/**
	 * 解密jwt
	 *
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public  Claims parseJWT(String jwt) throws Exception {
		SecretKey key = generalKey();
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		return claims;
	}

	@Override
	public TokenUserProfile getUser(String token) {
		if (token == null || token.trim().equals("") || token.split("\\.").length != 3) {
			return null;
		}
		TokenUserProfile readValue=null;
		Claims parseJWT;
		try {
			parseJWT = this.parseJWT(token);
			String subject = parseJWT.getSubject();
			ObjectMapper objectMapper = new ObjectMapper();
			readValue = objectMapper.readValue(subject, TokenUserProfile.class);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return readValue;
	}
}
