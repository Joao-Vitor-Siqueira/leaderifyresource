package jv.example.leaderifyresource.service;

import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.BusinessRuleViolationException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.model.LeaderboardInvitation;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.model.UserLeaderboard;
import jv.example.leaderifyresource.record.request.LeaderboardInvitationRequest;
import jv.example.leaderifyresource.record.response.LeaderboardInvitationResponse;
import jv.example.leaderifyresource.repository.LeaderboardInvitationRepository;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.UserLeaderboardRepository;
import jv.example.leaderifyresource.repository.UserRepository;
import jv.example.leaderifyresource.utils.InvitationStatus;
import jv.example.leaderifyresource.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaderboardInvitationService {
    private final LeaderboardInvitationRepository leaderboardInvitationRepository;
    private final UserLeaderboardRepository userLeaderboardRepository;
    private final UserRepository userRepository;
    private final LeaderboardRepository leaderboardRepository;
    private final  UserLeaderboardService userLeaderboardService;

    public LeaderboardInvitationResponse createLeaderboardInvitation(
            LeaderboardInvitationRequest request,
            Long leaderboardId,
            Long authenticatedUserId
    ) {
        Optional<UserLeaderboard> issuer = userLeaderboardRepository.findByUserIdAndLeaderboardId(authenticatedUserId,leaderboardId);
        if(!issuer.isPresent() || !issuer.get().getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(UserLeaderboard.class.getSimpleName());
        }
        Optional<UserLeaderboard> exists =
                userLeaderboardRepository.findByUserIdAndLeaderboardId(request.userId(),leaderboardId);
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(UserLeaderboard.class.getSimpleName());
        }

        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        User.class.getSimpleName()
                ));

        LeaderboardInvitation  leaderboardInvitation = new LeaderboardInvitation();
        leaderboardInvitation.setUser(user);
        leaderboardInvitation.setLeaderboard(leaderboard);
        leaderboardInvitation.setStatus(InvitationStatus.PENDING);

        if(request.role().equals(Role.ADMIN) && !issuer.get().getId().equals(leaderboard.getOwner().getId())) {
            throw new BusinessRuleViolationException("Admins cannot create other admins");
        }
        leaderboardInvitation.setRole(request.role());
        LeaderboardInvitation savedLeaderboardInvitation = leaderboardInvitationRepository.save(leaderboardInvitation);

        return new LeaderboardInvitationResponse(
                savedLeaderboardInvitation.getId(),
                InvitationStatus.PENDING
        );
    }

    public void updateLeaderboardInvitation(Long id, InvitationStatus status, Long authenticatedUserId) {
        LeaderboardInvitation leaderboardInvitation = leaderboardInvitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        LeaderboardInvitation.class.getSimpleName()
                ));

        if(!authenticatedUserId.equals(leaderboardInvitation.getUser().getId())) {
            throw new AccessDeniedException(UserLeaderboard.class.getSimpleName());
        }
        leaderboardInvitation.setStatus(status);
        LeaderboardInvitation savedLeaderboardInvitation = leaderboardInvitationRepository.save(leaderboardInvitation);

        if(status.equals(InvitationStatus.ACCEPTED)) {
            userLeaderboardService.createUserLeaderboard(savedLeaderboardInvitation);
        }
    }
}
