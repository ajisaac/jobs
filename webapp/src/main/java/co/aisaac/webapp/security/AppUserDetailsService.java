package co.aisaac.webapp.security;

import co.aisaac.webapp.User;
import co.aisaac.webapp.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findAllByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found.");
        }

        AppUserPrincipal principal = new AppUserPrincipal();
        principal.setAccountNonExpired(!user.isAccountExpired());
        principal.setAccountNonLocked(!user.isAccountLocked());
        principal.setCredentialsNonExpired(!user.isCredentialsExpired());
        principal.setEnabled(user.isEnabled());
        principal.setUsername(user.getEmail());
        principal.setPassword(user.getPassword());

        String authString = user.getAuthorities();
        List<SimpleGrantedAuthority> auths = Arrays.stream(authString.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(SimpleGrantedAuthority::new)
                .toList();
        principal.setAuthorities(auths);

        return principal;

    }
}
