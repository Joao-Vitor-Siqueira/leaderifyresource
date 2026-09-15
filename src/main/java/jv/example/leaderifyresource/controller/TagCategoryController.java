package jv.example.leaderifyresource.controller;

import jv.example.leaderifyresource.record.request.TagCategoryRequest;
import jv.example.leaderifyresource.record.response.TagCategoryResponse;
import jv.example.leaderifyresource.service.TagCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards/{leaderboardId}/tagCategories")
public class TagCategoryController {
    private final TagCategoryService tagCategoryService;

    @GetMapping("/{id}")
    public ResponseEntity<TagCategoryResponse> getTagCategory(@PathVariable Long id){
        return ResponseEntity.ok(tagCategoryService.findTagCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TagCategoryResponse>> getTagCategories(Pageable pageable){
        return ResponseEntity.ok(tagCategoryService.listTagCategories(pageable));
    }

    @PostMapping
    public ResponseEntity<TagCategoryResponse> createTagCategory(@RequestBody TagCategoryRequest tagCategoryRequest, @PathVariable Long leaderboardId) {
        return ResponseEntity.ok(tagCategoryService.createTagCategory(tagCategoryRequest, leaderboardId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagCategoryResponse> updateTagCategory(@PathVariable Long id, @RequestBody TagCategoryRequest tagCategoryRequest) {
        return ResponseEntity.ok(tagCategoryService.updateTagCategory(id, tagCategoryRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagCategory(@PathVariable Long id){
        tagCategoryService.deleteTagCategory(id);
    }
}
