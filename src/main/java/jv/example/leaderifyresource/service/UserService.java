package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.config.CustomUserDetails;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.RefreshToken;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.record.response.AuthResponse;
import jv.example.leaderifyresource.record.response.UsernameResponse;
import jv.example.leaderifyresource.repository.RefreshTokenRepository;
import jv.example.leaderifyresource.repository.UserRepository;
import jv.example.leaderifyresource.utils.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;


    public Page<User> listUsers(String name, Pageable pageable, Long authenticatedUserId){
        if(!userRepository.existsByIdAndAuthority(authenticatedUserId, Authority.ADMIN)){
           throw new AccessDeniedException(User.class.getSimpleName());
        }
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
            throw new ResourceAlreadyExistsException(STR."User already exists with email:\{userRequest.getEmail()}");
        }
        if(userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResourceAlreadyExistsException(STR."User already exists with username:\{userRequest.getUsername()}");
        }

        String hashPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getUsername(), hashPassword , userRequest.getEmail(), Authority.USER);
        return userRepository.save(user);
    }

    public User getUser(Long id, Long authenticatedUserId) {
        if(!userRepository.existsByIdAndAuthority(authenticatedUserId, Authority.ADMIN)){
            throw new AccessDeniedException(User.class.getSimpleName());
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName()));
    }

    @Transactional
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

        User savedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName()));

        RefreshToken entity = new RefreshToken();
        entity.setUser(savedUser);
        entity.setToken(jwtService.hashToken(refreshToken));
        entity.setRevoked(false);
        entity.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(entity);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Token is not a refresh token");
        }

        String tokenHash = jwtService.hashToken(refreshToken);

        RefreshToken storedToken = refreshTokenRepository.findByToken(tokenHash)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(RefreshToken.class.getSimpleName()));

        if (storedToken.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        Long userId =  jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName()));

        String newAccessToken =
                jwtService.generateAccessToken(user.getEmail());

        String newRefreshToken =
                jwtService.generateRefreshToken(userId);

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setToken(jwtService.hashToken(newRefreshToken));
        entity.setRevoked(false);
        entity.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(entity);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken
        );
    }

    public void logout(String refreshToken) {

        if (refreshToken == null) {
            return;
        }

        String tokenHash = jwtService.hashToken(refreshToken);

        refreshTokenRepository.findByToken(tokenHash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UsernameResponse findUsernameById(Long id) {
        String username = userRepository.findUsernameById(id).orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName()));
        return new UsernameResponse(username);
    }
}
