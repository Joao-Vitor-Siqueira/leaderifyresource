package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.record.request.*;
import jv.example.leaderifyresource.record.response.LeaderboardResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.UserLeaderboardRepository;
import jv.example.leaderifyresource.repository.UserRepository;
import jv.example.leaderifyresource.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;
    private final UserLeaderboardRepository userLeaderboardRepository;
    private final UserRepository userRepository;

    @Transactional
    public LeaderboardResponse createLeaderboard(LeaderboardRequest request, Long authenticatedUserId) {
        Leaderboard leaderboard = new Leaderboard();

        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());

        for (MetricRequest metricRequest : request.metrics()) {
            Metric metric = new Metric();
            metric.setName(metricRequest.name());
            metric.setDescription(metricRequest.description());
            leaderboard.addMetric(metric);
        }

        for (EntryRequest entryRequest : request.entries()) {
            Entry entry = new Entry();
            entry.setName(entryRequest.name());
            entry.setDescription(entryRequest.description());
            leaderboard.addEntry(entry);
        }

        for (TagCategoryRequest  tagCategoryRequest : request.tagCategories()) {
            TagCategory tagCategory = new TagCategory();
            tagCategory.setName(tagCategoryRequest.name());
            tagCategory.setDescription(tagCategoryRequest.description());
            for(TagRequest tagRequest : tagCategoryRequest.tags()){
                Tag tag = new Tag();
                tag.setName(tagRequest.name());
                tag.setDescription(tagRequest.description());
                tagCategory.addTag(tag);
            }
            leaderboard.addTagCategory(tagCategory);
        }

        Leaderboard savedLeaderboard = leaderboardRepository.save(leaderboard);

        User user = userRepository.getReferenceById(authenticatedUserId);
        UserLeaderboard userLeaderboard = new UserLeaderboard();
        userLeaderboard.setLeaderboard(savedLeaderboard);
        userLeaderboard.setUser(user);
        userLeaderboard.setRole(Role.OWNER);
        userLeaderboardRepository.save(userLeaderboard);

        return new LeaderboardResponse(
                savedLeaderboard.getId(),
                savedLeaderboard.getName(),
                savedLeaderboard.getDescription()
        );
    }

    public LeaderboardResponse findLeaderboardById(Long id) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), id
                ));

        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription()
        );
    }

    public Page<LeaderboardResponse> listLeaderboards(Pageable pageable){
        return leaderboardRepository.findAll(pageable)
                .map(leaderboard -> new LeaderboardResponse(
                        leaderboard.getId(),
                        leaderboard.getName(),
                        leaderboard.getDescription()
                ));
    }

    @Transactional
    public LeaderboardResponse updateLeaderboard(Long id, LeaderboardRequest request) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), id
                ));

        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());


        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription()
        );
    }

    @Transactional
    public void deleteLeaderboard(Long id) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), id
                ));

        leaderboardRepository.delete(leaderboard);
    }
}
