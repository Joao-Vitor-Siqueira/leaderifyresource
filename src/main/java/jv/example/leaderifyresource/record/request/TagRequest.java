package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.model.EntryTag;
import jv.example.leaderifyresource.model.TagCategory;

import java.util.List;

public record TagRequest(
        String name,
        String description,
        TagCategory tagCategory,
        List<EntryTag> entryTags
) {
}
