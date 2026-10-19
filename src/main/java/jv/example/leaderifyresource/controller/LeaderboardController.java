package jv.example.leaderifyresource.controller;

import jakarta.validation.Valid;
import jv.example.leaderifyresource.record.request.LeaderboardCreationRequest;
import jv.example.leaderifyresource.record.request.LeaderboardPatchRequest;
import jv.example.leaderifyresource.record.response.LeaderboardResponse;
import jv.example.leaderifyresource.service.LeaderboardService;
import jv.example.leaderifyresource.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController{
    private final LeaderboardService leaderboardService;

    @GetMapping("/{id}")
    public ResponseEntity<LeaderboardResponse> getLeaderboard(@PathVariable Long id){
        return ResponseEntity.ok(leaderboardService.findLeaderboardById(id));
    }

    @GetMapping
    public ResponseEntity<Page<LeaderboardResponse>> listLeaderboards(Pageable pageable,@RequestParam(required = false) String name){
        return ResponseEntity.ok(leaderboardService.listLeaderboards(pageable, name));
    }

    @GetMapping("/owned")
    public ResponseEntity<Page<LeaderboardResponse>> listOwnedLeaderboards(Pageable pageable,@RequestParam(required = false) String name){
        return ResponseEntity.ok(leaderboardService.listLeaderboardsByOwner(pageable, name, SecurityUtils.getCurrentUserId()));
    }

    @PostMapping
    public ResponseEntity<LeaderboardResponse> createLeaderboard(@Valid @RequestBody LeaderboardCreationRequest request){
        return ResponseEntity.ok(leaderboardService.createLeaderboard(request, SecurityUtils.getCurrentUserId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LeaderboardResponse> updateLeaderboard(@PathVariable Long id,@RequestBody LeaderboardPatchRequest request){
        return ResponseEntity.ok(leaderboardService.updateLeaderboard(id, request, SecurityUtils.getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLeaderboard(@PathVariable Long id) {
        leaderboardService.deleteLeaderboard(id, SecurityUtils.getCurrentUserId());
    }

    @GetMapping("/leaderboard-exists")
    public ResponseEntity<Boolean> leaderboardExists(@RequestParam String name) {
        boolean exists = leaderboardService.existsByOwner_IdAndName(SecurityUtils.getCurrentUserId(), name);
        return ResponseEntity.ok(exists);
    }

}
