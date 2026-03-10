package nhom02.doanmon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import nhom02.doanmon.entity.User;
import nhom02.doanmon.repository.UserRepository;
import nhom02.doanmon.repository.RoleRepository;
import nhom02.doanmon.entity.Role;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String provider = userRequest.getClientRegistration().getRegistrationId();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setFullName(name);
            user.setProvider(provider.toUpperCase());
            user.setPassword(""); // No password for OAuth users

            Role role = roleRepository.findByName("USER").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("USER");
                return roleRepository.save(newRole);
            });
            user.setRoles(new java.util.HashSet<>(Collections.singleton(role)));
            user = userRepository.save(user);
        }

        if (user != null && !user.getEnabled()) {
            throw new OAuth2AuthenticationException(new org.springframework.security.oauth2.core.OAuth2Error(
                    "user_disabled", "User account is disabled", null));
        }

        java.util.Set<org.springframework.security.core.GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }
}
