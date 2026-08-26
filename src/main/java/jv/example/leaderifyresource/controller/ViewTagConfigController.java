package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.ViewTagConfigRequest;
import jv.example.leaderifyresource.record.response.ViewTagConfigResponse;
import jv.example.leaderifyresource.service.ViewTagConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaderboards/{leaderboardId}/views/{viewId}/tags")
public class ViewTagConfigController {
    private final ViewTagConfigService viewTagConfigService;

    @GetMapping("/{id}")
    public ResponseEntity<ViewTagConfigResponse> getViewTagConfig(@PathVariable Long id){
        return ResponseEntity.ok(viewTagConfigService.findViewTagConfigById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ViewTagConfigResponse>> getViewTagConfigs(Pageable pageable){
        return ResponseEntity.ok(viewTagConfigService.listViewTagConfigs(pageable));
    }

    @PostMapping
    public ResponseEntity<ViewTagConfigResponse> createViewTagConfig(@RequestBody ViewTagConfigRequest request) {
        return ResponseEntity.ok(viewTagConfigService.createViewTagConfig(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ViewTagConfigResponse> updateViewTagConfig(@PathVariable Long id, @RequestBody ViewTagConfigRequest request) {
        return ResponseEntity.ok(viewTagConfigService.updateViewTagConfig(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteViewTagConfig(@PathVariable Long id){
        viewTagConfigService.deleteViewTagConfig(id);
    }
}
