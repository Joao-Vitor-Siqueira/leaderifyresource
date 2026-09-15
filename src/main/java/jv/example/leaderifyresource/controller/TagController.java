package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.response.TagResponse;
import jv.example.leaderifyresource.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/tagCategories/{tagCategoryId}/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTag(@PathVariable Long id){
        return ResponseEntity.ok(tagService.findTagById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TagResponse>> getTags(Pageable pageable){
        return ResponseEntity.ok(tagService.listTags(pageable));
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody String name, @PathVariable Long tagCategoryId) {
        return ResponseEntity.ok(tagService.createTag(name, tagCategoryId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long id, @RequestBody String name) {
        return ResponseEntity.ok(tagService.updateTag(id, name));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id){
        tagService.deleteTag(id);
    }

}
