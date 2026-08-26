package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.model.Tag;

import java.util.List;

public record TagCategoryRequest(
        String name,
        String description,
        List<Tag> tags
) {
}
