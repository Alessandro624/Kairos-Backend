package it.unical.demacs.informatica.KairosBackend.core.service;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.debug("Loading OAuth2 user for registration ID: {}",
                oAuth2UserRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        log.debug("Retrieving user information for provider: {}", provider);
        User user = getUserByOAuth2User(oAuth2User.getAttributes(), provider);

        log.info("Successfully loaded OAuth2 user: {}", user.getUsername());
        return getOAuth2User(oAuth2User, user);
    }

    public OAuth2User getOAuth2User(OAuth2User oAuth2User, User user) {
        log.debug("Creating custom OAuth2User instance for user: {}", user.getUsername());
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

    public User getUserByOAuth2User(Map<String, Object> attributes, Provider provider) {
        log.debug("Processing OAuth2 user attributes");
        String email = (String) attributes.get("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found");
        }

        log.debug("Searching for existing user with email: {}", email);
        return userRepository.findByEmail(email)
                .map(existingUser -> validateProvider(existingUser, provider))
                .orElseGet(() -> createNewUser(attributes, provider, email));
    }

    private User validateProvider(User existingUser, Provider provider) {
        log.debug("Validating provider for existing user: {}", existingUser.getUsername());
        if (existingUser.getProvider() == provider) {
            return existingUser;
        } else {
            throw new OAuth2AuthenticationException("User already registered with another provider");
        }
    }

    private User createNewUser(Map<String, Object> attributes, Provider provider, String email) {
        log.debug("Creating new user with email: {} for provider: {}", email, provider);
        User user = new User();
        user.setEmail(email);
        user.setUsername(email.split("@")[0]);
        user.setPassword(UUID.randomUUID().toString());
        user.setFirstName(attributes.get("given_name").toString());
        user.setLastName(attributes.get("family_name").toString());
        user.setEmailVerified(true);
        user.setProvider(provider);
        user.setRole(extractUserRole(extractRoles(attributes)));
        return userRepository.save(user);
    }

    private UserRole extractUserRole(Collection<GrantedAuthority> authorities) {
        log.debug("Extracting user role from authorities");
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.replace("ROLE_", "").toUpperCase())
                .filter(roleName -> {
                    try {
                        UserRole.valueOf(roleName);
                        return true;
                    } catch (IllegalArgumentException ex) {
                        return false;
                    }
                })
                .map(UserRole::valueOf)
                .findFirst()
                .orElse(UserRole.USER);
    }

    private Collection<GrantedAuthority> extractRoles(Map<String, Object> claims) {
        log.debug("Extracting roles from claims");
        Set<GrantedAuthority> authorities = new HashSet<>();
        extractRolesFromRealmAccess(claims, authorities);
        extractRolesFromResourceAccess(claims, authorities);
        return authorities;
    }

    private void extractRolesFromRealmAccess(Map<String, Object> claims, Set<GrantedAuthority> authorities) {
        Map<String, Object> realmAccess = getMap(claims, "realm_access");
        if (realmAccess != null) {
            log.debug("Processing realm access roles");
            List<String> roles = getList(realmAccess);
            addRoles(roles, authorities);
        }
    }

    @SuppressWarnings("unchecked")
    private void extractRolesFromResourceAccess(Map<String, Object> claims, Set<GrantedAuthority> authorities) {
        Map<String, Object> resourceAccess = getMap(claims, "resource_access");
        if (resourceAccess != null) {
            log.debug("Processing resource access roles");
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
            log.debug("Adding {} roles to authorities", roles.size());
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
