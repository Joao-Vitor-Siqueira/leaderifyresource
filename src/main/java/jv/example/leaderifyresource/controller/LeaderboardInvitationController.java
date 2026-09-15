package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.LeaderboardInvitationRequest;
import jv.example.leaderifyresource.record.response.LeaderboardInvitationResponse;
import jv.example.leaderifyresource.record.response.UserLeaderboardResponse;
import jv.example.leaderifyresource.service.LeaderboardInvitationService;
import jv.example.leaderifyresource.utils.InvitationStatus;
import jv.example.leaderifyresource.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/members/invite")
public class LeaderboardInvitationController {
    private final LeaderboardInvitationService leaderboardInvitationService;

    @PostMapping
    public ResponseEntity<LeaderboardInvitationResponse> createLeaderboardInvitation(
            @RequestBody LeaderboardInvitationRequest request,
            @PathVariable Long leaderboardId
    ) {
        return ResponseEntity.ok(leaderboardInvitationService.createLeaderboardInvitation(request, leaderboardId, SecurityUtils.getCurrentUserId()));
    }

    @PatchMapping("/{id}")
    public void updateLeaderboardInvitation(@PathVariable Long id, @RequestBody InvitationStatus invitationStatus){
        leaderboardInvitationService.updateLeaderboardInvitation(id,invitationStatus, SecurityUtils.getCurrentUserId());
    }
}
