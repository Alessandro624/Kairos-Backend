package it.unical.demacs.informatica.KairosBackend.config.security;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        try {
            User user = getUserByOAuth2User(oAuth2User, provider);

            if (user.getProvider() != provider) {
                throw new OAuth2AuthenticationException("User with email " + user.getEmail() + " already exists");
            }

            return getOAuth2User(oAuth2User, user);

        } catch (Exception e) {
            throw new OAuth2AuthenticationException("Failed to load user information");
        }
    }

    public OAuth2User getOAuth2User(OAuth2User oAuth2User, User user) {
        return new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return oAuth2User.getAttributes();
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
            }

            @Override
            public String getName() {
                return user.getUsername();
            }
        };
    }

    public User getUserByOAuth2User(OAuth2User oAuth2User, Provider provider) {
        String email = (String) oAuth2User.getAttributes().get("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found in OAuth2 provider");
        }

        String firstName = (String) oAuth2User.getAttributes().get("given_name");
        String lastName = (String) oAuth2User.getAttributes().get("family_name");
        // String pictureUrl = (String) oAuth2User.getAttributes().get("picture");
        boolean emailVerified = (boolean) oAuth2User.getAttributes().get("email_verified");

        Collection<GrantedAuthority> authorities = extractRoles(oAuth2User.getAttributes());

        UserRole role = authorities.isEmpty() ? UserRole.USER : UserRole.valueOf(authorities.iterator().next().getAuthority().replace("ROLE_", "").toUpperCase());

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email.split("@")[0]);
                    newUser.setPassword(UUID.randomUUID().toString());
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setRole(role);
                    newUser.setProvider(provider);
                    newUser.setEmailVerified(emailVerified);
                    return userRepository.save(newUser);
                });
    }

    private Collection<GrantedAuthority> extractRoles(Map<String, Object> claims) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        extractRolesFromRealmAccess(claims, authorities);
        extractRolesFromResourceAccess(claims, authorities);
        return authorities;
    }

    private void extractRolesFromRealmAccess(Map<String, Object> claims, Set<GrantedAuthority> authorities) {
        Map<String, Object> realmAccess = getMap(claims, "realm_access");
        if (realmAccess != null) {
            List<String> roles = getList(realmAccess);
            addRoles(roles, authorities);
        }
    }

    @SuppressWarnings("unchecked")
    private void extractRolesFromResourceAccess(Map<String, Object> claims, Set<GrantedAuthority> authorities) {
        Map<String, Object> resourceAccess = getMap(claims, "resource_access");
        if (resourceAccess != null) {
            resourceAccess.values().forEach(clientAccess -> {
                if (clientAccess instanceof Map) {
                    List<String> roles = getList((Map<String, Object>) clientAccess);
                    addRoles(roles, authorities);
                }
            });
        }
    }

    private void addRoles(List<String> roles, Set<GrantedAuthority> authorities) {
        if (roles != null) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMap(Map<String, Object> claims, String key) {
        Object value = claims.get(key);
        return value instanceof Map ? (Map<String, Object>) value : null;
    }

    @SuppressWarnings("unchecked")
    private static List<String> getList(Map<String, Object> claims) {
        Object value = claims.get("roles");
        return value instanceof List ? (List<String>) value : null;
    }
}
