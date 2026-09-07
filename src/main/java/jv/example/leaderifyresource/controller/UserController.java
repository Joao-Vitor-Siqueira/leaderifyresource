package jv.example.leaderifyresource.controller;

import jakarta.servlet.http.HttpServletResponse;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.record.response.AuthResponse;
import jv.example.leaderifyresource.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Value("${jwt.cookie.secure}")
    private boolean secureCookie;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<Page<User>> listUsers(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        return ResponseEntity.ok(
                userService.listUsers(name, pageable)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody User request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = userService.login(request);

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", authResponse.refreshToken())
                .httpOnly(true)
                .secure(secureCookie) // true in prod
                .sameSite("Strict")
                .path("/api/v1/users")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        STR."Bearer \{authResponse.accessToken()}"
                )
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        AuthResponse authResponse =
                userService.refreshToken(refreshToken);

        ResponseCookie newRefreshCookie = ResponseCookie
                .from("refreshToken", authResponse.refreshToken())
                .httpOnly(true)
                .secure(secureCookie) // true in prod
                .sameSite("Strict")
                .path("/api/v1/users")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                newRefreshCookie.toString()
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        STR."Bearer \{authResponse.accessToken()}"
                )
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        userService.logout(refreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite("Strict")
                .path("/api/v1/users")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }


}
