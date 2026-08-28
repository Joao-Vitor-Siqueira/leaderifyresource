package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.record.request.ViewRequest;
import jv.example.leaderifyresource.record.response.ViewResponse;
import jv.example.leaderifyresource.repository.*;
import jv.example.leaderifyresource.utils.LeaderboardUtils;
import jv.example.leaderifyresource.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewService {
    private final ViewRepository viewRepository;
    private final UserLeaderboardRepository userLeaderboardRepository;

    @Transactional
    public ViewResponse createView(ViewRequest request, Long userLeaderboardId) {
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(userLeaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName(), userLeaderboardId
                ));
        Role userRole = userLeaderboard.getRole();

        if(!LeaderboardUtils.canCreateView(userRole, userLeaderboardId)) {
            throw new AccessDeniedException(View.class.getSimpleName());
        }

        Optional<View> exists =
                viewRepository.findByNameAndUserLeaderboardId(request.name(),userLeaderboardId);
        if (exists.isPresent())
            throw new ResourceAlreadyExistsException(View.class.getSimpleName());

        View view = new View();

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

        Role userRole = view.getUserLeaderboard().getRole();
        Long leaderboardOwnerId = view.getUserLeaderboard().getLeaderboard().getOwner().getId();
        Long viewOwnerId = view.getUserLeaderboard().getId();

        if(!LeaderboardUtils.canUpdateOrDeleteView(userRole, viewOwnerId, leaderboardOwnerId)){
            throw new AccessDeniedException(View.class.getSimpleName());
        }

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

        Role userRole = view.getUserLeaderboard().getRole();
        Long leaderboardOwnerId = view.getUserLeaderboard().getLeaderboard().getOwner().getId();
        Long viewOwnerId = view.getUserLeaderboard().getId();


        if(!LeaderboardUtils.canUpdateOrDeleteView(userRole, viewOwnerId, leaderboardOwnerId)){
            throw new AccessDeniedException(View.class.getSimpleName());
        }

        viewRepository.delete(view);
    }
}
