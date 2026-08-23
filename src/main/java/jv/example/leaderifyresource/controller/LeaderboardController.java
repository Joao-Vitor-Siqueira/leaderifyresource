package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.LeaderboardRequest;
import jv.example.leaderifyresource.record.response.LeaderboardResponse;
import jv.example.leaderifyresource.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController{
    private final LeaderboardService leaderboardService;

    @GetMapping("/{id}")
    public ResponseEntity<LeaderboardResponse> getLeaderboard(@PathVariable Long id){
        return ResponseEntity.ok(leaderboardService.findLeaderboardById(id));
    }

    @GetMapping
    public ResponseEntity<Page<LeaderboardResponse>> listLeaderboards(Pageable pageable){
        return ResponseEntity.ok(leaderboardService.listLeaderboards(pageable));
    }

    @PostMapping
    public ResponseEntity<LeaderboardResponse> createLeaderboard(LeaderboardRequest request){
        return ResponseEntity.ok(leaderboardService.createLeaderboard(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LeaderboardResponse> updateLeaderboard(@PathVariable Long id,LeaderboardRequest request){
        return ResponseEntity.ok(leaderboardService.updateLeaderboard(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLeaderboard(@PathVariable Long id) {
        leaderboardService.deleteLeaderboard(id);
    }

}
