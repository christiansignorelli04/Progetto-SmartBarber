package com.smartbarber.smartbarber.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // estraggo i ruoli dal token JWT
        Collection<GrantedAuthority> roles = extractRealmRoles(jwt);
        return new JwtAuthenticationToken(jwt, roles, getPrincipalClaimName(jwt));
    }

    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return Set.of();
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
        // converto i ruoli in "Authority" per Spring
        return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
    }

    private String getPrincipalClaimName(Jwt jwt) {
        return jwt.getClaimAsString("preferred_username"); // usa l'username come id
    }
}