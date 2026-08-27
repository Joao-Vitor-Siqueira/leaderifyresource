package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.model.Tag;
import jv.example.leaderifyresource.model.TagCategory;
import jv.example.leaderifyresource.record.request.TagCategoryRequest;
import jv.example.leaderifyresource.record.request.TagRequest;
import jv.example.leaderifyresource.record.response.TagCategoryResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.TagCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagCategoryService {
    private final TagCategoryRepository tagCategoryRepository;
    private final LeaderboardRepository leaderboardRepository;

    public TagCategoryResponse createTagCategory(TagCategoryRequest request, Long leaderboardId) {
        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), leaderboardId
                ));
        TagCategory tagCategory = new TagCategory();
        tagCategory.setName(request.name());
        tagCategory.setDescription(request.description());
        tagCategory.setLeaderboard(leaderboard);


        for(TagRequest tagRequest : request.tags()) {
            Tag tag = new Tag();
            tag.setName(tagRequest.name());
            tag.setDescription(tagRequest.description());

            tagCategory.addTag(tag);
        }

        TagCategory savedTagCategory = tagCategoryRepository.save(tagCategory);

        return new TagCategoryResponse(
                savedTagCategory.getId(),
                savedTagCategory.getName(),
                savedTagCategory.getDescription()
        );
    }

    public TagCategoryResponse findTagCategoryById(Long id) {
        TagCategory tagCategory = tagCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TagCategory.class.getSimpleName(), id
                ));
        return new TagCategoryResponse(
                tagCategory.getId(),
                tagCategory.getName(),
                tagCategory.getDescription()
        );
    }

    public Page<TagCategoryResponse> listTagCategories(Pageable pageable) {
        return tagCategoryRepository.findAll(pageable)
                .map(tagCategory -> new TagCategoryResponse(
                        tagCategory.getId(),
                        tagCategory.getName(),
                        tagCategory.getDescription()
                ));
    }

    @Transactional
    public TagCategoryResponse updateTagCategory(Long id, TagCategoryRequest request) {
        TagCategory tagCategory = tagCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TagCategory.class.getSimpleName(), id
                ));

        tagCategory.setName(request.name());
        tagCategory.setDescription(request.description());

        return new TagCategoryResponse(
                tagCategory.getId(),
                tagCategory.getName(),
                tagCategory.getDescription()
        );
    }

    @Transactional
    public void deleteTagCategory(Long id) {
        TagCategory tagCategory = tagCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TagCategory.class.getSimpleName(), id
                ));
        tagCategoryRepository.delete(tagCategory);
    }
}
