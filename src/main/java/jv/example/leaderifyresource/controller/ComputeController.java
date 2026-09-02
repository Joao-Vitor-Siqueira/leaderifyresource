package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.response.CalculationResponse;
import jv.example.leaderifyresource.service.ComputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/members/{userLeaderboardId}/views/{viewId}/compute")
@RequiredArgsConstructor
public class ComputeController {
    private final ComputeService computeService;

    @GetMapping
    public ResponseEntity<CalculationResponse> getCalculationResult(@PathVariable Long leaderboardId,@PathVariable Long viewId){
        return ResponseEntity.ok(computeService.calculate(leaderboardId,viewId));
    }

}
