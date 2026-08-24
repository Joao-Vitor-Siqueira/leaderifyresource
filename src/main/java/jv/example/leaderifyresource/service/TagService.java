package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Tag;
import jv.example.leaderifyresource.record.request.TagRequest;
import jv.example.leaderifyresource.record.response.TagResponse;
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

    @Transactional
    public TagResponse createTag(TagRequest request) {
        Optional<Tag> exists =
                tagRepository.findByNameAndTagCategoryId(request.name(),request.tagCategory().getId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(Tag.class.getSimpleName(), exists.get().getId());
        }
        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setDescription(request.description());
        tag.setTagCategory(request.tagCategory());
        tag.setEntryTags(request.entryTags());
        Tag savedTag = tagRepository.save(tag);

        return new TagResponse(
                savedTag.getId(),
                savedTag.getName(),
                savedTag.getDescription(),
                savedTag.getTagCategory(),
                savedTag.getEntryTags()
        );
    }

    public TagResponse findTagById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return new TagResponse(
                    tag.get().getId(),
                    tag.get().getName(),
                    tag.get().getDescription(),
                    tag.get().getTagCategory(),
                    tag.get().getEntryTags()
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
                        tag.getTagCategory(),
                        tag.getEntryTags()
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
        tag.setTagCategory(request.tagCategory());
        tag.setEntryTags(request.entryTags());

        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getDescription(),
                tag.getTagCategory(),
                tag.getEntryTags()
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
