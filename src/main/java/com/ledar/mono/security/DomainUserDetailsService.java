package com.ledar.mono.security;

import com.ledar.mono.domain.User;
import com.ledar.mono.repository.UserRepository;
import com.ledar.mono.repository.UserRoleRepository;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public DomainUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository
            .findOneByLogin(lowercaseLogin)
            .map(this::createSpringSecurityUser)
            .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }

    private UserModel createSpringSecurityUser(User user) {
        List<GrantedAuthority> authorities = userRoleRepository
            .getAllRoleCodeByUserId(user.getId())
            //.findAllByUserId(user.getId())
            .stream()
            //.map(SimpleGrantedAuthority::new)
            .map(roleCode->new SimpleGrantedAuthority(roleCode))
            //.map(userRole -> new SimpleGrantedAuthority(userRole.getRoleCode()))
            .distinct()
            .collect(Collectors.toList());
        return new UserModel(user.getLogin(), user.getPassword(), authorities,user.getId(),user.getUserStatus());
    }

}
