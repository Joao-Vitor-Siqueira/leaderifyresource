package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.config.CustomUserDetails;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.record.response.AuthResponse;
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

    @Transactional
    public User createUser(User userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException();
        }
        String hashPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getUsername(), hashPassword , userRequest.getEmail());
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName()));
    }

    public AuthResponse login(User request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        String username = authentication.getName();

        String accessToken =
                jwtService.generateAccessToken(username);

        String refreshToken =
                jwtService.generateRefreshToken(user.getId());

        return new AuthResponse(accessToken, refreshToken);
    }


    public AuthResponse refreshToken(String refreshToken) {

        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Token is not a refresh token");
        }

        Long userId =  jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName()));

        String newAccessToken =
                jwtService.generateAccessToken(user.getEmail());

        String newRefreshToken =
                jwtService.generateRefreshToken(userId);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken
        );
    }
}
