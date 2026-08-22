package jv.example.leaderifyresource.service;

import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Page<User> listUsers(String name, Pageable pageable) {
        Page<User> users;
        if (name == null || name.isBlank()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByUsernameContainingIgnoreCase(name, pageable);
        }
        return users;
    }

    public User createUser(User userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException();
        }
        String hashPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getUsername(), hashPassword , userRequest.getEmail());
        return userRepository.save(user);
    }

    public String login(User request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String username = authentication.getName();
        String token = jwtService.generateToken(username);

        return token;
    }
}
