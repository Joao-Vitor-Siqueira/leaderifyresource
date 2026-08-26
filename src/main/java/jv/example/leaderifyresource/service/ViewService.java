package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.model.UserLeaderboard;
import jv.example.leaderifyresource.model.View;
import jv.example.leaderifyresource.record.request.ViewRequest;
import jv.example.leaderifyresource.record.response.ViewResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.UserLeaderboardRepository;
import jv.example.leaderifyresource.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewService {
    private final ViewRepository viewRepository;
    private final LeaderboardRepository leaderboardRepository;
    private final UserLeaderboardRepository userLeaderboardRepository;

    @Transactional
    public ViewResponse createView(ViewRequest request) {
        Leaderboard leaderboard = leaderboardRepository.findById(request.leaderboardId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), request.leaderboardId()
                ));
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(request.userLeaderboardId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName(), request.userLeaderboardId()
                ));
        Optional<View> exists =
                viewRepository.findByNameAndLeaderboardId(request.name(),request.leaderboardId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(View.class.getSimpleName(), exists.get().getId());
        }

        View view = new View();
        view.setLeaderboard(leaderboard);
        view.setName(request.name());
        view.setUserLeaderboard(userLeaderboard);
        view.setViewMetricConfigs(request.viewMetricConfigs());
        view.setViewTagConfigs(request.viewTagConfigs());
        view.setVisibility(request.visibility());
        View savedView = viewRepository.save(view);

        return new ViewResponse(
                savedView.getId(),
                request.name(),
                leaderboard.getId(),
                userLeaderboard.getId(),
                request.viewMetricConfigs(),
                request.viewTagConfigs(),
                request.visibility()
        );
    }

    public ViewResponse findViewById(Long id) {
        View view =
                viewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName(), id
                ));
        return new ViewResponse(
                view.getId(),
                view.getName(),
                view.getLeaderboard().getId(),
                view.getUserLeaderboard().getId(),
                view.getViewMetricConfigs(),
                view.getViewTagConfigs(),
                view.getVisibility()
        );
    }

    public Page<ViewResponse> listViews(Pageable pageable) {
        return viewRepository.findAll(pageable)
                .map(view -> new ViewResponse(
                        view.getId(),
                        view.getName(),
                        view.getLeaderboard().getId(),
                        view.getUserLeaderboard().getId(),
                        view.getViewMetricConfigs(),
                        view.getViewTagConfigs(),
                        view.getVisibility()
                ));
    }

    @Transactional
    public ViewResponse updateView(Long id, ViewRequest request) {
        View view = viewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName(), id
                ));

        view.setName(request.name());
        view.setViewTagConfigs(request.viewTagConfigs());
        view.setViewMetricConfigs(request.viewMetricConfigs());
        view.setVisibility(request.visibility());

        return new ViewResponse(
                view.getId(),
                view.getName(),
                view.getLeaderboard().getId(),
                view.getUserLeaderboard().getId(),
                view.getViewMetricConfigs(),
                view.getViewTagConfigs(),
                view.getVisibility()
        );
    }

    @Transactional
    public void deleteView(Long id) {
        View view = viewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName(), id
                ));
        viewRepository.delete(view);
    }
}
