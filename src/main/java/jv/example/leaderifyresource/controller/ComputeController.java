package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.PageResponse;
import jv.example.leaderifyresource.record.response.CalculationResponse;
import jv.example.leaderifyresource.service.ComputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/members/{userLeaderboardId}/views/{viewId}/compute")
@RequiredArgsConstructor
public class ComputeController {
    private final ComputeService computeService;

    @GetMapping
    public ResponseEntity<PageResponse<CalculationResponse>> getCalculationResult(@PathVariable Long leaderboardId, @PathVariable Long viewId){
        CalculationResponse response = computeService.calculate(leaderboardId, viewId);
        PageResponse.Sort sort = new PageResponse.Sort(false,true,false);
        PageResponse.Pageable pageable = new PageResponse.Pageable(1,1,1,true,false,sort);

        PageResponse<CalculationResponse> pageResponse = new PageResponse<CalculationResponse>(
                List.of(response),
                false,
                true,
                true,
                0,
                1,
                1,
                1,
                1,
                pageable,
                sort

        );
        return ResponseEntity.ok(pageResponse);
    }

}
