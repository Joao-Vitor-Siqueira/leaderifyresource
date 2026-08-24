package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
    public ResponseEntity<Void> login(@RequestBody User request) {
        String token = userService.login(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, STR."Bearer \{token}").build();
    }
}
