package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.UserRelationshipRequest;
import jv.example.leaderifyresource.record.response.UserRelationshipResponse;
import jv.example.leaderifyresource.service.UserRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userRelationships")
public class UserRelationshipController {
    private final UserRelationshipService userRelationshipService;

    @GetMapping("/{id}")
    public ResponseEntity<UserRelationshipResponse> getUserRelationship(@PathVariable Long id){
        return ResponseEntity.ok(userRelationshipService.findUserRelationshipById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserRelationshipResponse>> getUserRelationships(Pageable pageable){
        return ResponseEntity.ok(userRelationshipService.listUserRelationships(pageable));
    }

    @PostMapping
    public ResponseEntity<UserRelationshipResponse> createUserRelationship(@RequestBody UserRelationshipRequest request) {
        return ResponseEntity.ok(userRelationshipService.createUserRelationship(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserRelationshipResponse> updateUserRelationship(@PathVariable Long id, @RequestBody UserRelationshipRequest request) {
        return ResponseEntity.ok(userRelationshipService.updateUserRelationship(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserRelationship(@PathVariable Long id){
        userRelationshipService.deleteUserRelationship(id);
    }
}
