package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.BusinessRuleViolationException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.record.request.*;
import jv.example.leaderifyresource.record.response.LeaderboardResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.UserLeaderboardRepository;
import jv.example.leaderifyresource.repository.UserRepository;
import jv.example.leaderifyresource.repository.ViewRepository;
import jv.example.leaderifyresource.utils.Role;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;
    private final UserLeaderboardRepository userLeaderboardRepository;
    private final UserRepository userRepository;
    private final ViewRepository viewRepository;

    @Transactional
    public LeaderboardResponse createLeaderboard(LeaderboardCreationRequest request, Long authenticatedUserId) {
        if(request.metrics().size() != request.metricSorts().size()){
            throw new BusinessRuleViolationException("Every metric must have a configuration");
        }
        if(leaderboardRepository.existsByOwner_IdAndName(authenticatedUserId, request.name())){
            throw new ResourceAlreadyExistsException(Leaderboard.class.getSimpleName());
        }

        // Leaderboard and UserLeaderboard creation

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());

        for (MetricRequest metricRequest : request.metrics()) {
            Metric metric = new Metric();
            metric.setName(metricRequest.name());
            metric.setDescription(metricRequest.description());
            leaderboard.addMetric(metric);
        }

        User user = userRepository.getReferenceById(authenticatedUserId);
        leaderboard.setOwner(user);

        UserLeaderboard userLeaderboard = new UserLeaderboard();
        userLeaderboard.setLeaderboard(leaderboard);
        userLeaderboard.setUser(user);
        userLeaderboard.setRole(Role.ADMIN);

        Leaderboard savedLeaderboard = leaderboardRepository.save(leaderboard);
        userLeaderboardRepository.save(userLeaderboard);

        // Base View Creation

        View view = new View();
        view.setName(request.name());
        view.setUserLeaderboard(userLeaderboard);
        view.setLeaderboard(leaderboard);
        view.setVisibility(Visibility.SHOW);

        List<Metric> savedMetrics = savedLeaderboard.getMetrics();

        for (int i = 0; i < request.metricSorts().size(); i++) {
            Sort sort = request.metricSorts().get(i);
            Metric metric = savedMetrics.get(i);
            ViewMetricConfig viewMetricConfig = new ViewMetricConfig();
            viewMetricConfig.setSort(sort);
            viewMetricConfig.setMetric(metric);
            viewMetricConfig.setPosition(i);
            viewMetricConfig.setVisibility(Visibility.SHOW);
            view.addViewMetricConfig(viewMetricConfig);
        }
        viewRepository.save(view);

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
    public LeaderboardResponse updateLeaderboard(Long id, LeaderboardPatchRequest request, Long authenticatedUserId) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), id
                ));
        if(!leaderboard.getOwner().getId().equals(authenticatedUserId)){
            throw new AccessDeniedException(Leaderboard.class.getSimpleName());
        }

        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());


        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription()
        );
    }

    @Transactional
    public void deleteLeaderboard(Long id, Long authenticatedUserId) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), id
                ));
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findByUserIdAndLeaderboardId(authenticatedUserId, id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName(), id
                ));

        userLeaderboardRepository.delete(userLeaderboard);
        leaderboardRepository.delete(leaderboard);
    }
}
