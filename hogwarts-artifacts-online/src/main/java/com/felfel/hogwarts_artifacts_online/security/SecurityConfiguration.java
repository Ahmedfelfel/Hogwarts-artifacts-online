package com.felfel.hogwarts_artifacts_online.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


/**
 * The type Security configuration.
 */
@Configuration
public class SecurityConfiguration {

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;

    /**
     * Instantiates a new Security configuration.
     *
     * @param customBasicAuthenticationEntryPoint       the custom basic authentication entry point
     * @param customBearerTokenAccessDeniedHandler      the custom bearer token access denied handler
     * @param customBearerTokenAuthenticationEntryPoint the custom bearer token authentication entry point
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public SecurityConfiguration(CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler, CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint) throws NoSuchAlgorithmException {
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey= (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * Security filter chain security filter chain.
     *
     * @param httpSecurity the http security
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(HttpMethod.GET, baseUrl + "/artifacts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, baseUrl + "/users/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, baseUrl + "/users").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, baseUrl + "/users/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, baseUrl + "/users/**").hasAuthority("ROLE_admin")
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(
                        header->header
                                .contentSecurityPolicy(csp->csp
                                        .policyDirectives("frame-ancestors 'self'"))
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(http->http.authenticationEntryPoint(customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2->oauth2
                        .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(customBearerTokenAccessDeniedHandler))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();

    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Jwt encoder jwt encoder.
     *
     * @return the jwt encoder
     */
    @Bean
    public JwtEncoder jwtEncoder ()
    {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    /**
     * Jwt decoder jwt decoder.
     *
     * @return the jwt decoder
     */
    @Bean
    public JwtDecoder jwtDecoder()
    {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    /**
     * Jwt authentication converter jwt authentication converter.
     *
     * @return the jwt authentication converter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
