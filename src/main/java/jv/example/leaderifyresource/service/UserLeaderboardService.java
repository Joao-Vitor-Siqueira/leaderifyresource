package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.model.UserLeaderboard;
import jv.example.leaderifyresource.record.request.UserLeaderboardRequest;
import jv.example.leaderifyresource.record.response.UserLeaderboardResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.UserLeaderboardRepository;
import jv.example.leaderifyresource.repository.UserRepository;
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
    public UserLeaderboardResponse createUserLeaderboard(UserLeaderboardRequest request) {
        Optional<UserLeaderboard> exists =
                userLeaderboardRepository.findByUserIdAndLeaderboardId(request.userId(),request.leaderboardId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(UserLeaderboard.class.getSimpleName(), exists.get().getId());
        }

        Leaderboard leaderboard = leaderboardRepository.findById(request.leaderboardId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName(), request.leaderboardId()
                ));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        User.class.getSimpleName(), request.userId()
                ));


        UserLeaderboard userLeaderboard = new UserLeaderboard();
        userLeaderboard.setUser(user);
        userLeaderboard.setLeaderboard(leaderboard);
        userLeaderboard.setRole(request.role());
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
                        UserLeaderboard.class.getSimpleName(), id
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
    public UserLeaderboardResponse updateUserLeaderboard(Long id, UserLeaderboardRequest request) {
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName(), id
                ));

        userLeaderboard.setRole(request.role());

        return new UserLeaderboardResponse(
                userLeaderboard.getId(),
                userLeaderboard.getUser().getId(),
                userLeaderboard.getLeaderboard().getId(),
                userLeaderboard.getRole()
        );
    }

    @Transactional
    public void deleteUserLeaderboard(Long id) {
        UserLeaderboard userLeaderboard = userLeaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserLeaderboard.class.getSimpleName(), id
                ));
        userLeaderboardRepository.delete(userLeaderboard);
    }
}
