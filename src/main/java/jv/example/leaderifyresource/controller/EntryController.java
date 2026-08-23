package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.EntryRequest;
import jv.example.leaderifyresource.record.response.EntryResponse;
import jv.example.leaderifyresource.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EntryController {
    private final EntryService entryService;

    @GetMapping("/entries/{id}")
    public ResponseEntity<EntryResponse> getEntry(@PathVariable Long id){
        return ResponseEntity.ok(entryService.findEntryById(id));
    }

    @GetMapping("/entries")
    public ResponseEntity<Page<EntryResponse>> getEntries(Pageable pageable){
        return ResponseEntity.ok(entryService.listEntries(pageable));
    }

    @PostMapping("/entries")
    public ResponseEntity<EntryResponse> createEntry(EntryRequest request) {
        return ResponseEntity.ok(entryService.createEntry(request));
    }

    @PatchMapping("/entries/{id}")
    public ResponseEntity<EntryResponse> updateEntry(@PathVariable Long id, @RequestBody EntryRequest request) {
        return ResponseEntity.ok(entryService.updateEntry(id, request));
    }

    @DeleteMapping("/entries/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable Long id){
        entryService.deleteEntry(id);
    }

}
