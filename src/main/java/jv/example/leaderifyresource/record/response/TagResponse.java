package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.model.EntryTag;
import jv.example.leaderifyresource.model.TagCategory;

import java.util.List;

public record TagResponse(
        Long id,
        String name,
        String description,
        TagCategory tagCategory,
        List<EntryTag> entryTags
) {
}
