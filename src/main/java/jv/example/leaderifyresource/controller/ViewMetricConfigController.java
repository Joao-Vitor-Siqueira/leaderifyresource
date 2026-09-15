package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.ViewMetricConfigRequest;
import jv.example.leaderifyresource.record.response.ViewMetricConfigResponse;
import jv.example.leaderifyresource.service.ViewMetricConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/members/{userLeaderboardId}/views/{viewId}/metrics/{metricId}")
public class ViewMetricConfigController {
    private final ViewMetricConfigService viewMetricConfigService;

    @GetMapping("/{id}")
    public ResponseEntity<ViewMetricConfigResponse> getViewMetricConfig(@PathVariable Long id){
        return ResponseEntity.ok(viewMetricConfigService.findViewMetricConfigById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ViewMetricConfigResponse>> getViewMetricConfigs(Pageable pageable){
        return ResponseEntity.ok(viewMetricConfigService.listViewMetricConfigs(pageable));
    }

    @PostMapping
    public ResponseEntity<ViewMetricConfigResponse> createViewMetricConfig(
            @RequestBody ViewMetricConfigRequest request,
            @PathVariable Long viewId,
            @PathVariable Long metricId) {
        return ResponseEntity.ok(viewMetricConfigService.createViewMetricConfig(request, viewId, metricId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ViewMetricConfigResponse> updateViewMetricConfig(@PathVariable Long id, @RequestBody ViewMetricConfigRequest request) {
        return ResponseEntity.ok(viewMetricConfigService.updateViewMetricConfig(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteViewMetricConfig(@PathVariable Long id){
        viewMetricConfigService.deleteViewMetricConfig(id);
    }
}
