package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.record.request.MetricRequest;
import jv.example.leaderifyresource.record.response.MetricResponse;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricService {
    private final MetricRepository metricRepository;
    private final LeaderboardRepository leaderboardRepository;

    @Transactional
    public MetricResponse createMetric(MetricRequest request, Long leaderboardId) {
        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));

        Metric metric = new Metric();
        metric.setName(request.name());
        metric.setDescription(request.description());
        metric.setLeaderboard(leaderboard);
        Metric savedMetric = metricRepository.save(metric);

        return new MetricResponse(
                savedMetric.getId(),
                savedMetric.getName(),
                savedMetric.getDescription()
        );
    }

    public MetricResponse findMetricById(Long id) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));
        return new MetricResponse(
                metric.getId(),
                metric.getName(),
                metric.getDescription()
        );
    }

    public Page<MetricResponse> listMetrics(Pageable pageable, Long leaderboardId) {
        return metricRepository.findAllByLeaderboardId(leaderboardId,pageable)
                .map(metric -> new MetricResponse(
                        metric.getId(),
                        metric.getName(),
                        metric.getDescription()
                ));
    }

    @Transactional
    public MetricResponse updateMetric(Long id,MetricRequest request) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));

        metric.setName(request.name());
        metric.setDescription(request.description());

        return new MetricResponse(
                metric.getId(),
                metric.getName(),
                metric.getDescription()
        );
    }

    @Transactional
    public void deleteMetric(Long id) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));
        metricRepository.delete(metric);
    }
}
