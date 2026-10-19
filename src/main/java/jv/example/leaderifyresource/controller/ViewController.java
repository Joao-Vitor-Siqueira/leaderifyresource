package jv.example.leaderifyresource.controller;

import jakarta.validation.Valid;
import jv.example.leaderifyresource.record.request.ViewRequest;
import jv.example.leaderifyresource.record.response.ViewResponse;
import jv.example.leaderifyresource.service.ViewService;
import jv.example.leaderifyresource.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/members/{userLeaderboardId}/views")
public class ViewController {
    private final ViewService viewService;

    @GetMapping("/{id}")
    public ResponseEntity<ViewResponse> getView(@PathVariable Long id){
        return ResponseEntity.ok(viewService.findViewById(id));
    }

    @GetMapping("/base")
    public ResponseEntity<ViewResponse> getBaseView(@PathVariable Long leaderboardId){
        return ResponseEntity.ok(viewService.findBaseView(leaderboardId));
    }

    @GetMapping
    public ResponseEntity<Page<ViewResponse>> getViews(Pageable pageable){
        return ResponseEntity.ok(viewService.listViews(pageable));
    }

    @PostMapping
    public ResponseEntity<ViewResponse> createView(@Valid @RequestBody ViewRequest request, @PathVariable Long userLeaderboardId) {
        return ResponseEntity.ok(viewService.createView(request, userLeaderboardId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ViewResponse> updateView(@PathVariable Long id, @RequestBody ViewRequest request, @PathVariable Long userLeaderboardId) {
        return ResponseEntity.ok(viewService.updateView(id, request, userLeaderboardId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteView(@PathVariable Long id) {
        viewService.deleteView(id);
    }
}
