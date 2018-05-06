package com.xhcoding.helper;

import com.xhcoding.entity.User;
import com.xhcoding.exception.LoginException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xin on 2018/1/24.
 */
@Slf4j
public class JWTHelper {
    private static final long serialVersionUID = -3301605591108950415L;
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_ID = "id";
    static final String CLAIM_KEY_TYPE = "type";

    private static final String AUDIENCE_UNKNOWN = "UNKNOWN";
    private static final String AUDIENCE_PC = "PC";
    private static final String AUDIENCE_H5 = "H5";
    private static final String AUDIENCE_ANDROID = "ANDROID";
    private static final String AUDIENCE_IOS = "IOS";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.prefix}")
    private String prefix;

    String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        try {
            RedisUtils.put(String.format("JWT_TOKEN_%S", claims.get(CLAIM_KEY_ID).toString()), token, expiration.intValue());
        } catch (Throwable e) {
            log.error("JwtTokenHelper.generateToken exception", e);
        }
        return token;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Throwable e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Throwable e) {
            created = null;
        }
        return created;
    }

    public String getType(String token) {
        String type;
        try {
            final Claims claims = getClaimsFromToken(token);
            type = (String) claims.get(CLAIM_KEY_TYPE);
        } catch (Throwable e) {
            type = null;
        }
        return type;
    }

    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = Long.parseLong(claims.get(CLAIM_KEY_ID).toString());
        } catch (Throwable e) {
            userId = null;
        }
        return userId;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Throwable e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Throwable e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Throwable e) {
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private String generateAudience(int source) {
        String audience = AUDIENCE_UNKNOWN;
        if (0 == source) {
            audience = AUDIENCE_PC;
        } else if (1 == source) {
            audience = AUDIENCE_ANDROID;
        } else if (2 == source) {
            audience = AUDIENCE_IOS;
        } else if (3 == source) {
            audience = AUDIENCE_H5;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_H5.equals(audience) || AUDIENCE_IOS.equals(audience) || AUDIENCE_ANDROID.equals(audience) || AUDIENCE_PC.equals(audience));
    }

    public String generateToken(User user, int source) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_ID, user.getId());
        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(source));
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_TYPE, user.getType());
        return generateToken(claims);
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Throwable e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String getToken(HttpServletRequest httpServletRequest) throws Exception {
        //从头部中获取token
        String authToken = httpServletRequest.getHeader(this.tokenHeader);
        if (!StringUtils.isEmpty(authToken) && (authToken.contains(prefix))) {
            return authToken.substring(7);
        } else {
            //从参数中获取token
            String requestToken = httpServletRequest.getParameter(this.tokenHeader);
            if (!StringUtils.isEmpty(requestToken) && requestToken.contains(prefix)) {
                return requestToken.substring(7);
            }
        }
        return null;
    }

    public void validateSign(String authToken) throws Exception {
        // 从redis 中获取
        Long userId = getUserIdFromToken(authToken);
        String redisToken = RedisUtils.get(String.format("JWT_TOKEN_%s", userId));
        if (StringUtils.isEmpty(redisToken)) {
            throw new LoginException("当前账号登录信息已失效,请重新登录!");
        }

        if (!redisToken.equals(authToken)) {
            String audienceFromToken = getAudienceFromToken(redisToken);
            throw new LoginException(String.format("当前账号已在%s登录, 请重新登录!", audienceFromToken));
        }
    }
}
