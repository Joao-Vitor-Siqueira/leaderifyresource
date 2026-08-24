package jv.example.leaderifyresource.service;

import jv.example.leaderifyresource.config.CustomUserDetails;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new
                UsernameNotFoundException(STR."Usuário não existe com o email: \{email}"));
        return new CustomUserDetails(user);
    }
}
