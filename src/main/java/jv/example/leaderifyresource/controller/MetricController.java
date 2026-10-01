package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.MetricRequest;
import jv.example.leaderifyresource.record.response.MetricResponse;
import jv.example.leaderifyresource.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/metrics")
public class MetricController {
    private final MetricService metricService;

    @GetMapping("/{id}")
    public ResponseEntity<MetricResponse> getMetric(@PathVariable Long id){
        return ResponseEntity.ok(metricService.findMetricById(id));
    }

    @GetMapping
    public ResponseEntity<Page<MetricResponse>> getMetrics(Pageable pageable, @PathVariable Long leaderboardId){
        return ResponseEntity.ok(metricService.listMetrics(pageable, leaderboardId));
    }

    @PostMapping
    public ResponseEntity<MetricResponse> createMetric(@RequestBody MetricRequest request, @PathVariable Long leaderboardId) {
        return ResponseEntity.ok(metricService.createMetric(request, leaderboardId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MetricResponse> updateMetric(@PathVariable Long id, @RequestBody MetricRequest request) {
        return ResponseEntity.ok(metricService.updateMetric(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMetric(@PathVariable Long id){
        metricService.deleteMetric(id);
    }
}
