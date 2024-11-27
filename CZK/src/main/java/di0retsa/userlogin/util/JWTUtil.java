package di0retsa.userlogin.util;

import di0retsa.userlogin.entity.JWTProperties;
import di0retsa.userlogin.entity.exception.ErrorTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT token工具类
 */
@Component
@RequiredArgsConstructor
public class JWTUtil {
    private final JWTProperties jwtProperties;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 生成Token User一小时后过期 Admin两小时后过期
     * @param role 角色
     * @param claims 用户信息Map
     * @return Token
     */
    public String createJWT(Integer role, Map<String, Object> claims) {
        if(role == 0){
            long ttl = System.currentTimeMillis() + jwtProperties.getUserTtl();
            Date date = new Date(ttl);
            JwtBuilder builder = Jwts.builder()
                    .setClaims(claims)
                    .signWith(signatureAlgorithm, jwtProperties.getUserSecretKey().getBytes(StandardCharsets.UTF_8))
                    .setExpiration(date);
            return jwtProperties.getUserTokenPrefix() + builder.compact();
        }else{
            long ttl = System.currentTimeMillis() + jwtProperties.getAdminTtl();
            Date date = new Date(ttl);
            JwtBuilder builder = Jwts.builder()
                    .setClaims(claims)
                    .signWith(signatureAlgorithm, jwtProperties.getAdminSecretKey().getBytes(StandardCharsets.UTF_8))
                    .setExpiration(date);
            return jwtProperties.getAdminTokenPrefix() + builder.compact();
        }
    }

    public Map<String, Object> paresJWT(String jwtToken) throws ErrorTokenException {
        if (!jwtToken.startsWith(jwtProperties.getAdminTokenPrefix()) && !jwtToken.startsWith(jwtProperties.getUserTokenPrefix())) {
            throw new ErrorTokenException();
        }
        int role;
        if(jwtToken.startsWith(jwtProperties.getUserTokenPrefix())){
            jwtToken = jwtToken.replace(jwtProperties.getUserTokenPrefix(), "");
            role = 0;
        } else {
            jwtToken = jwtToken.replace(jwtProperties.getAdminTokenName(), "");
            role = 1;
        }
        if(role == 0){
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getUserSecretKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } else {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getAdminSecretKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(jwtToken)
                    .getBody();
        }
    }
}
