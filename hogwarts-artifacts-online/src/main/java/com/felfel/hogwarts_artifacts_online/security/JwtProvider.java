package com.felfel.hogwarts_artifacts_online.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/**
 * The type Jwt provider.
 */
@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    /**
     * Instantiates a new Jwt provider.
     *
     * @param jwtEncoder the jwt encoder
     */
    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Create token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        long expire = 2; // 2 hour
        //claims
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("hogwarts-artifacts-online")
                .issuedAt(now)
                .expiresAt(now.plus(expire, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
