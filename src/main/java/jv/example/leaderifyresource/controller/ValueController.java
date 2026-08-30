package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.ValueRequest;
import jv.example.leaderifyresource.record.response.ValueResponse;
import jv.example.leaderifyresource.service.ValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaderboards/{leaderboardId}/values")
public class ValueController {
    private final ValueService valueService;

    @GetMapping("/{id}")
    public ResponseEntity<ValueResponse> getValue(@PathVariable Long id){
        return ResponseEntity.ok(valueService.findValueById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ValueResponse>> getValues(Pageable pageable){
        return ResponseEntity.ok(valueService.listValues(pageable));
    }

    @PostMapping
    public ResponseEntity<ValueResponse> createValue(@RequestBody ValueRequest request) {
        return ResponseEntity.ok(valueService.createValue(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ValueResponse> updateValue(@PathVariable Long id, @RequestBody ValueRequest request) {
        return ResponseEntity.ok(valueService.updateValue(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteValue(@PathVariable Long id){
        valueService.deleteValue(id);
    }
}
