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
public class MetricController {
    private final MetricService metricService;

    @GetMapping("/metrics/{id}")
    public ResponseEntity<MetricResponse> getMetric(@PathVariable Long id){
        return ResponseEntity.ok(metricService.findMetricById(id));
    }

    @GetMapping("/metrics")
    public ResponseEntity<Page<MetricResponse>> getMetrics(Pageable pageable){
        return ResponseEntity.ok(metricService.listMetrics(pageable));
    }

    @PostMapping("/leaderboards/{leaderboardId}/metrics")
    public ResponseEntity<MetricResponse> createMetric(@PathVariable Long leaderboardId, MetricRequest metricRequest) {
        return ResponseEntity.ok(metricService.createMetric(leaderboardId ,metricRequest));
    }

    @PatchMapping("/metrics/{id}")
    public ResponseEntity<MetricResponse> updateMetric(@PathVariable Long id, @RequestBody MetricRequest metricRequest) {
        return ResponseEntity.ok(metricService.updateMetric(id, metricRequest));
    }

    @DeleteMapping("/metrics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMetric(@PathVariable Long id){
        metricService.deleteMetric(id);
    }
}
