package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Tag;
import jv.example.leaderifyresource.model.TagCategory;
import jv.example.leaderifyresource.record.request.TagRequest;
import jv.example.leaderifyresource.record.response.TagResponse;
import jv.example.leaderifyresource.repository.TagCategoryRepository;
import jv.example.leaderifyresource.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagCategoryRepository tagCategoryRepository;

    @Transactional
    public TagResponse createTag(TagRequest request, Long tagCategoryId) {
        TagCategory tagCategory = tagCategoryRepository.findById(tagCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TagCategory.class.getSimpleName(), tagCategoryId
                ));

        Optional<Tag> exists =
                tagRepository.findByNameAndTagCategoryId(request.name(),tagCategoryId);
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(Tag.class.getSimpleName(), exists.get().getId());
        }

        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setDescription(request.description());
        tag.setTagCategory(tagCategory);
        Tag savedTag = tagRepository.save(tag);

        return new TagResponse(
                savedTag.getId(),
                savedTag.getName(),
                savedTag.getDescription(),
                tagCategoryId
        );
    }

    public TagResponse findTagById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return new TagResponse(
                    tag.get().getId(),
                    tag.get().getName(),
                    tag.get().getDescription(),
                    tag.get().getTagCategory().getId()
            );
        }
        throw new ResourceNotFoundException(Tag.class.getSimpleName(), id);
    }

    public Page<TagResponse> listTags(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(tag -> new TagResponse(
                        tag.getId(),
                        tag.getName(),
                        tag.getDescription(),
                        tag.getTagCategory().getId()
                ));
    }

    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Tag.class.getSimpleName(), id
                ));

        tag.setName(request.name());
        tag.setDescription(request.description());

        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getDescription(),
                tag.getTagCategory().getId()
        );
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Tag.class.getSimpleName(), id
                ));
        tagRepository.delete(tag);
    }
}
