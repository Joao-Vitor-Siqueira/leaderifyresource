package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.record.request.LeaderboardRequest;
import jv.example.leaderifyresource.record.response.LeaderboardResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;

    @Transactional
    public LeaderboardResponse createLeaderboard(LeaderboardRequest request) {
        Leaderboard leaderboard = new Leaderboard();

        leaderboard.setName(request.name());
        leaderboard.setDescription(request.description());
        leaderboard.setMetrics(request.metrics());
        leaderboard.setEntries(request.entries());

        Leaderboard savedLeaderboard = leaderboardRepository.save(leaderboard);

        return new LeaderboardResponse(
                savedLeaderboard.getId(),
                savedLeaderboard.getName(),
                savedLeaderboard.getDescription(),
                savedLeaderboard.getMetrics(),
                savedLeaderboard.getEntries()
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
                leaderboard.getDescription(),
                leaderboard.getMetrics(),
                leaderboard.getEntries()
        );
    }

    public Page<LeaderboardResponse> listLeaderboards(Pageable pageable){
        return leaderboardRepository.findAll(pageable)
                .map(leaderboard -> new LeaderboardResponse(
                        leaderboard.getId(),
                        leaderboard.getName(),
                        leaderboard.getDescription(),
                        leaderboard.getMetrics(),
                        leaderboard.getEntries()
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
        leaderboard.setMetrics(request.metrics());

        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDescription(),
                leaderboard.getMetrics(),
                leaderboard.getEntries()
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
