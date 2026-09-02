package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.response.UserLeaderboardResponse;
import jv.example.leaderifyresource.service.UserLeaderboardService;
import jv.example.leaderifyresource.utils.Role;
import jv.example.leaderifyresource.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/members")
public class UserLeaderboardController {
    private final UserLeaderboardService userLeaderboardService;

    @GetMapping("/{id}")
    public ResponseEntity<UserLeaderboardResponse> getUserLeaderboard(@PathVariable Long id){
        return ResponseEntity.ok(userLeaderboardService.findUserLeaderboardById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserLeaderboardResponse>> getUserLeaderboards(Pageable pageable){
        return ResponseEntity.ok(userLeaderboardService.listUserLeaderboards(pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserLeaderboardResponse> updateUserLeaderboard(@PathVariable Long id, @RequestBody Role role, @PathVariable Long leaderboardId) {
        return ResponseEntity.ok(userLeaderboardService.updateUserLeaderboard(id, role, leaderboardId, SecurityUtils.getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserLeaderboard(@PathVariable Long id, @PathVariable Long leaderboardId){
        userLeaderboardService.deleteUserLeaderboard(id, leaderboardId, SecurityUtils.getCurrentUserId());
    }

    @DeleteMapping("leave/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveLeaderboard(@PathVariable Long id){
        userLeaderboardService.leaveLeaderboard(id, SecurityUtils.getCurrentUserId());
    }
}
