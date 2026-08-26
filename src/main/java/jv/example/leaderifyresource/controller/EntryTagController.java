package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.EntryTagRequest;
import jv.example.leaderifyresource.record.response.EntryTagResponse;
import jv.example.leaderifyresource.service.EntryTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entryTags")
public class EntryTagController {
    private final EntryTagService entryTagService;

    @GetMapping("/{id}")
    public ResponseEntity<EntryTagResponse> getEntryTag(@PathVariable Long id){
        return ResponseEntity.ok(entryTagService.findEntryTagById(id));
    }

    @GetMapping
    public ResponseEntity<Page<EntryTagResponse>> getEntryTags(Pageable pageable){
        return ResponseEntity.ok(entryTagService.listEntryTags(pageable));
    }

    @PostMapping
    public ResponseEntity<EntryTagResponse> createEntryTag(@RequestBody EntryTagRequest request) {
        return ResponseEntity.ok(entryTagService.createEntryTag(request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntryTag(@PathVariable Long id){
        entryTagService.deleteEntryTag(id);
    }
}
