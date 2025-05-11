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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("Failed to load user information");
        }
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

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email.split("@")[0]);
                    newUser.setPassword(UUID.randomUUID().toString());
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setRole(UserRole.USER);
                    newUser.setProvider(provider);
                    newUser.setEmailVerified(emailVerified);
                    return userRepository.save(newUser);
                });
    }
}
