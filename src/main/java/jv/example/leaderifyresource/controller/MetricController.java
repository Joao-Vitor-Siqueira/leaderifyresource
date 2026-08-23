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

    @PostMapping("/metrics")
    public ResponseEntity<MetricResponse> createMetric(MetricRequest request) {
        return ResponseEntity.ok(metricService.createMetric(request));
    }

    @PatchMapping("/metrics/{id}")
    public ResponseEntity<MetricResponse> updateMetric(@PathVariable Long id, @RequestBody MetricRequest request) {
        return ResponseEntity.ok(metricService.updateMetric(id, request));
    }

    @DeleteMapping("/metrics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMetric(@PathVariable Long id){
        metricService.deleteMetric(id);
    }
}
