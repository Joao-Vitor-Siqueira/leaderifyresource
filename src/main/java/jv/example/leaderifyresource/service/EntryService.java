package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.record.request.EntryRequest;
import jv.example.leaderifyresource.record.response.EntryResponse;
import jv.example.leaderifyresource.repository.EntryRepository;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepository;
    private final LeaderboardRepository leaderboardRepository;

    @Transactional
    public EntryResponse createEntry(EntryRequest request, Long leaderboardId) {
        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));

        Entry entry = new Entry();
        entry.setName(request.name());
        entry.setDescription(request.description());
        entry.setLeaderboard(leaderboard);
        Entry savedEntry = entryRepository.save(entry);

        return new EntryResponse(
                savedEntry.getId(),
                savedEntry.getName(),
                savedEntry.getDescription()
        );
    }

    public EntryResponse findEntryById(Long id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Entry.class.getSimpleName()
                ));
        return new EntryResponse(
                entry.getId(),
                entry.getName(),
                entry.getDescription()
        );
    }

    public Page<EntryResponse> listEntries(Pageable pageable) {
        return entryRepository.findAll(pageable)
                .map(entry -> new EntryResponse(
                        entry.getId(),
                        entry.getName(),
                        entry.getDescription()
                ));
    }

    @Transactional
    public EntryResponse updateEntry(Long id,EntryRequest request) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Entry.class.getSimpleName()
                ));

        entry.setName(request.name());
        entry.setDescription(request.description());

        return new EntryResponse(
                entry.getId(),
                entry.getName(),
                entry.getDescription()
        );
    }

    @Transactional
    public void deleteEntry(Long id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Entry.class.getSimpleName()
                ));
        entryRepository.delete(entry);
    }
}
