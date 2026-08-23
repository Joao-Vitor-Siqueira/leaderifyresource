package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.model.Tag;

import java.util.List;

public record TagCategoryResponse(
        Long id,
        String name,
        String description,
        List<Tag> tags
) {
}
