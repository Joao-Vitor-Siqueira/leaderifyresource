package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.BusinessRuleViolationException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.model.LeaderboardInvitation;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.model.UserLeaderboard;
import jv.example.leaderifyresource.record.response.UserLeaderboardResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.UserLeaderboardRepository;
import jv.example.leaderifyresource.repository.UserRepository;
import jv.example.leaderifyresource.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLeaderboardService {
    private final UserLeaderboardRepository userLeaderboardRepository;
    private final UserRepository userRepository;
    private final LeaderboardRepository leaderboardRepository;

    @Transactional
    public UserLeaderboardResponse createUserLeaderboard(LeaderboardInvitation leaderboardInvitation) {
        Long leaderboardId = leaderboardInvitation.getLeaderboard().getId();
        Long userId = leaderboardInvitation.getUser().getId();

        Optional<UserLeaderboard> exists =
                userLeaderboardRepository.findByUserIdAndLeaderboardId(userId, leaderboardId);
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException("User is already a member of the leaderboard");
        }

        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        User.class.getSimpleName()
                ));


        UserLeaderboard userLeaderboard = new UserLeaderboard();
        userLeaderboard.setUser(user);
        userLeaderboard.setLeaderboard(leaderboard);
        userLeaderboard.setRole(leaderboardInvitation.getRole());

        UserLeaderboard savedUserLeaderboard = userLeaderboardRepository.save(userLeaderboard);

        return new UserLeaderboardResponse(
               savedUserLeaderboard.getId(),
               user.getId(),
               leaderboard.getId(),
               savedUserLeaderboard.getRole()
        );
    }

    public UserLeaderboardResponse findUserLeaderboardById(Long id) {
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName()
                ));
        return new UserLeaderboardResponse(
                userLeaderboard.getId(),
                userLeaderboard.getUser().getId(),
                userLeaderboard.getLeaderboard().getId(),
                userLeaderboard.getRole()
        );
    }

    public UserLeaderboardResponse findUserLeaderboardByUserId(Long leaderboardId, Long authenticatedUserId) {
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findByUserIdAndLeaderboardId(leaderboardId,authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName()
                ));
        return new UserLeaderboardResponse(
                userLeaderboard.getId(),
                userLeaderboard.getUser().getId(),
                userLeaderboard.getLeaderboard().getId(),
                userLeaderboard.getRole()
        );
    }

    public Page<UserLeaderboardResponse> listUserLeaderboards(Pageable pageable) {
        return userLeaderboardRepository.findAll(pageable)
                .map(userLeaderboard -> new UserLeaderboardResponse(
                        userLeaderboard.getId(),
                        userLeaderboard.getUser().getId(),
                        userLeaderboard.getLeaderboard().getId(),
                        userLeaderboard.getRole()
                ));
    }

    @Transactional
    public UserLeaderboardResponse updateUserLeaderboard(Long id, Role role, Long  leaderboardId, Long authenticatedUserId) {
        Optional<UserLeaderboard> issuer = userLeaderboardRepository.findByUserIdAndLeaderboardId(authenticatedUserId,leaderboardId);
        if(!issuer.isPresent() || !issuer.get().getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(UserLeaderboard.class.getSimpleName());
        }
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName()
                ));

        Long ownerId = userLeaderboard.getLeaderboard().getOwner().getId();

        if(userLeaderboard.getUser().getId().equals(ownerId)){
            throw new BusinessRuleViolationException("Cannot update roles for the leaderboard's owner");
        }

        if(role.equals(Role.ADMIN) && !authenticatedUserId.equals(ownerId)) {
            throw new BusinessRuleViolationException("Only the owner of the leaderboard can promote or revoke administrators");
        }

        userLeaderboard.setRole(role);

        return new UserLeaderboardResponse(
                userLeaderboard.getId(),
                userLeaderboard.getUser().getId(),
                userLeaderboard.getLeaderboard().getId(),
                userLeaderboard.getRole()
        );
    }

    @Transactional
    public void deleteUserLeaderboard(Long id, Long leaderboardId, Long authenticatedUserId) {
        Optional<UserLeaderboard> issuer = userLeaderboardRepository.findByUserIdAndLeaderboardId(authenticatedUserId,leaderboardId);
        if(!issuer.isPresent() || !issuer.get().getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(UserLeaderboard.class.getSimpleName());
        }
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName()
                ));

        Long ownerId =  userLeaderboard.getLeaderboard().getOwner().getId();

        if(userLeaderboard.getRole().equals(Role.ADMIN) && !authenticatedUserId.equals(ownerId)) {
            throw new BusinessRuleViolationException("Only the owner of the leaderboard can remove administrators");
        }

        if(id.equals(ownerId)) {
            throw new BusinessRuleViolationException("Cannot delete the leaderboard's owner");
        }

        userLeaderboardRepository.delete(userLeaderboard);
    }

    public void leaveLeaderboard(Long id, Long authenticatedUserId) {
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName()
                ));
        if(authenticatedUserId.equals(userLeaderboard.getLeaderboard().getOwner().getId())) {
            throw new BusinessRuleViolationException("The owner cannot leave the leaderboard");
        }
        if(!userLeaderboard.getUser().getId().equals(authenticatedUserId)){
            throw new AccessDeniedException(UserLeaderboard.class.getSimpleName());
        }
        userLeaderboardRepository.delete(userLeaderboard);
    }
}
