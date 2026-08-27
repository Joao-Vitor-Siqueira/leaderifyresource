package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.record.request.ViewMetricConfigRequest;
import jv.example.leaderifyresource.record.request.ViewRequest;
import jv.example.leaderifyresource.record.request.ViewTagConfigRequest;
import jv.example.leaderifyresource.record.response.ViewResponse;
import jv.example.leaderifyresource.repository.*;
import jv.example.leaderifyresource.utils.Role;
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
    public ViewResponse createView(ViewRequest request, Long leaderboardId) {
        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), leaderboardId
                ));
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(request.userLeaderboardId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName(), request.userLeaderboardId()
                ));

        Role userRole = userLeaderboard.getRole();
        if(userRole.equals(Role.VIEWER))
            throw new AccessDeniedException(View.class.getSimpleName());

        Optional<View> exists =
                viewRepository.findByNameAndLeaderboardId(request.name(),leaderboardId);
        if (exists.isPresent())
            throw new ResourceAlreadyExistsException(View.class.getSimpleName());

        View view = new View();

        view.setLeaderboard(leaderboard);
        view.setName(request.name());
        view.setUserLeaderboard(userLeaderboard);
        view.setVisibility(request.visibility());
        View savedView = viewRepository.save(view);

        return new ViewResponse(
                savedView.getId(),
                request.name(),
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
                view.getVisibility()
        );
    }

    public Page<ViewResponse> listViews(Pageable pageable) {
        return viewRepository.findAll(pageable)
                .map(view -> new ViewResponse(
                        view.getId(),
                        view.getName(),
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
        view.setVisibility(request.visibility());

        return new ViewResponse(
                view.getId(),
                view.getName(),
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
