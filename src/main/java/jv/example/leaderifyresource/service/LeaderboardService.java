package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.BusinessRuleViolationException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.record.request.*;
import jv.example.leaderifyresource.record.response.LeaderboardResponse;
import jv.example.leaderifyresource.repository.*;
import jv.example.leaderifyresource.utils.Role;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;
    private final UserRepository userRepository;
    private final ViewMetricConfigRepository viewMetricConfigRepository;

    @Transactional
    public LeaderboardResponse createLeaderboard(LeaderboardCreationRequest request, Long authenticatedUserId) {
        if(request.metrics().size() != request.metricSorts().size()){
            throw new BusinessRuleViolationException("Every metric must have a configuration");
        }
        if(leaderboardRepository.existsByOwner_IdAndName(authenticatedUserId, request.name())){
            throw new ResourceAlreadyExistsException("Leaderboard already exists");
        }

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());

        User user = userRepository.getReferenceById(authenticatedUserId);
        leaderboard.setOwner(user);

        UserLeaderboard userLeaderboard = new UserLeaderboard();
        userLeaderboard.setUser(user);
        userLeaderboard.setRole(Role.ADMIN);

        leaderboard.addUserLeaderboard(userLeaderboard);

        View view = new View();
        view.setName(request.name());
        view.setVisibility(Visibility.SHOW);
        view.setBase(true);

        for (int i = 0; i < request.metricSorts().size(); i++) {
            Metric metric = new Metric();
            metric.setName(request.metrics().get(i).name());
            metric.setDescription(request.metrics().get(i).description());

            Sort sort = request.metricSorts().get(i);
            ViewMetricConfig viewMetricConfig = new ViewMetricConfig();
            viewMetricConfig.setSort(sort);
            viewMetricConfig.setMetric(metric);
            viewMetricConfig.setPosition(i);
            viewMetricConfig.setVisibility(Visibility.SHOW);

            view.addViewMetricConfig(viewMetricConfig);
            leaderboard.addMetric(metric);
        }
        userLeaderboard.addView(view);

        Leaderboard savedLeaderboard = leaderboardRepository.save(leaderboard);

        return new LeaderboardResponse(
                savedLeaderboard.getId(),
                savedLeaderboard.getName(),
                savedLeaderboard.getDescription(),
                savedLeaderboard.getCreatedAt(),
                savedLeaderboard.getUpdatedAt()
        );
    }

    public LeaderboardResponse findLeaderboardById(Long id) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));

        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription(),
                leaderboard.getCreatedAt(),
                leaderboard.getUpdatedAt()
        );
    }

    public Page<LeaderboardResponse> listLeaderboards(Pageable pageable, String name){
        Page<Leaderboard> leaderboardPage;
        if(name == null || name.isBlank()){
            leaderboardPage = leaderboardRepository.findAll(pageable);
        }
        else {
            leaderboardPage = leaderboardRepository.findByNameContainingIgnoreCase(pageable, name);
        }

        return leaderboardPage.map(leaderboard -> new LeaderboardResponse(
                        leaderboard.getId(),
                        leaderboard.getName(),
                        leaderboard.getDescription(),
                        leaderboard.getCreatedAt(),
                        leaderboard.getUpdatedAt()
                ));
    }

    public Page<LeaderboardResponse> listLeaderboardsByOwner(Pageable pageable, String name, Long authenticatedUserId){
        Page<Leaderboard> leaderboardPage;
        if(name == null || name.isBlank()){
            leaderboardPage = leaderboardRepository.findByOwnerId(authenticatedUserId, pageable);
        }
        else {
            leaderboardPage = leaderboardRepository.findByOwnerIdAndNameContainingIgnoreCase(authenticatedUserId, name, pageable);
        }
        return leaderboardPage.map(leaderboard -> new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription(),
                leaderboard.getCreatedAt(),
                leaderboard.getUpdatedAt()
        ));
    }

    @Transactional
    public LeaderboardResponse updateLeaderboard(Long id, LeaderboardPatchRequest request, Long authenticatedUserId) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));
        if(!leaderboard.getOwner().getId().equals(authenticatedUserId)){
            throw new AccessDeniedException(Leaderboard.class.getSimpleName());
        }

        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());


        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription(),
                leaderboard.getCreatedAt(),
                leaderboard.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteLeaderboard(Long id, Long authenticatedUserId) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));

        if(!leaderboard.getOwner().getId().equals(authenticatedUserId)){
            throw new AccessDeniedException(Leaderboard.class.getSimpleName());
        }

        viewMetricConfigRepository.deleteAllByLeaderboardId(id);
        leaderboardRepository.delete(leaderboard);
    }

    public boolean existsByOwner_IdAndName(Long authenticatedUserId, String name) {
        return leaderboardRepository.existsByOwner_IdAndName(authenticatedUserId, name);
    }
}
