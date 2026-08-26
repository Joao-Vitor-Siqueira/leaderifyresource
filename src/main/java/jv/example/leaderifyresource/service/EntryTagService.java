package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.EntryTag;
import jv.example.leaderifyresource.model.Tag;
import jv.example.leaderifyresource.record.request.EntryTagRequest;
import jv.example.leaderifyresource.record.response.EntryTagResponse;
import jv.example.leaderifyresource.repository.EntryRepository;
import jv.example.leaderifyresource.repository.EntryTagRepository;
import jv.example.leaderifyresource.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryTagService {
    private final EntryTagRepository entryTagRepository;
    private final EntryRepository entryRepository;
    private final TagRepository tagRepository;

    @Transactional
    public EntryTagResponse createEntryTag(EntryTagRequest request) {
        Entry entry = entryRepository.findById(request.entryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Entry.class.getSimpleName(), request.entryId()
                ));
        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Tag.class.getSimpleName(), request.tagId()
                ));
        Optional<EntryTag> exists =
                entryTagRepository.findByEntryIdAndTagId(request.entryId(),request.tagId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(EntryTag.class.getSimpleName(), exists.get().getId());
        }


        EntryTag entryTag = new EntryTag();
        entryTag.setEntry(entry);
        entryTag.setTag(tag);

        EntryTag savedEntryTag = entryTagRepository.save(entryTag);

        return new EntryTagResponse(
                savedEntryTag.getId(),
                entry.getId(),
                tag.getId()
        );
    }

    public EntryTagResponse findEntryTagById(Long id) {
        EntryTag entryTag = entryTagRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EntryTag.class.getSimpleName(), id));
        return new EntryTagResponse(
                entryTag.getId(),
                entryTag.getEntry().getId(),
                entryTag.getTag().getId()
        );
    }

    public Page<EntryTagResponse> listEntryTags(Pageable pageable) {
        return entryTagRepository.findAll(pageable)
                .map(entryTag -> new EntryTagResponse(
                        entryTag.getId(),
                        entryTag.getEntry().getId(),
                        entryTag.getTag().getId()
                ));
    }

    @Transactional
    public void deleteEntryTag(Long id) {
        EntryTag entryTag = entryTagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        EntryTag.class.getSimpleName(), id
                ));
        entryTagRepository.delete(entryTag);
    }
}
