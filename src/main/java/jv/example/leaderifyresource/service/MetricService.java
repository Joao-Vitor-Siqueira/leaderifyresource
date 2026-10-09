package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.BusinessRuleViolationException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.record.request.MetricRequest;
import jv.example.leaderifyresource.record.response.MetricResponse;
import jv.example.leaderifyresource.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricService {
    private final MetricRepository metricRepository;
    private final LeaderboardRepository leaderboardRepository;
    private final ValueRepository valueRepository;
    private final EntryRepository entryRepository;
    private ViewRepository viewRepository;
    private ViewMetricConfigRepository viewMetricConfigRepository;


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

        ViewMetricConfig viewMetricConfig = new ViewMetricConfig();
        View baseView = viewRepository.findByLeaderboardIdAndBaseTrue(leaderboardId).orElseThrow(() -> new ResourceNotFoundException(
                View.class.getSimpleName()
        ));
        viewMetricConfig.setView(baseView);
        viewMetricConfig.setSort(request.sort());
        viewMetricConfig.setPosition(metricRepository.countByLeaderboardId(leaderboardId) + 1);

        metric.addViewMetricConfig(viewMetricConfig);

        Metric savedMetric = metricRepository.save(metric);

        List<Entry> entries = entryRepository.findAllByLeaderboardId(leaderboardId);
        List<Value> values = new ArrayList<>();

        if(!entries.isEmpty()){
            for(Entry entry: entries){
                Value value = new Value();
                value.setMetric(savedMetric);
                value.setEntry(entry);
                value.setValue(0.0);
                values.add(value);
            }
        }

        if(!values.isEmpty()){
            valueRepository.saveAll(values);
        }

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
    public MetricResponse updateMetric(Long id,Long leaderboardId ,MetricRequest request) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));
        metric.setName(request.name());
        metric.setDescription(request.description());

        //base view: temp
        View view = viewRepository.findByLeaderboardIdAndBaseTrue(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName()
                ));

        Long viewId = view.getId();

        ViewMetricConfig viewMetricConfig = viewMetricConfigRepository.findByViewIdAndMetricId(viewId, id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ViewMetricConfig.class.getSimpleName()
                ));
        viewMetricConfig.setSort(request.sort());

        if(request.position() != viewMetricConfig.getPosition()){
            long totalMetrics = viewMetricConfigRepository.countByViewId(viewId);

            if(request.position() > totalMetrics){
                throw new BusinessRuleViolationException("Position exceeds total metrics");
            }

            ViewMetricConfig existingPosition = viewMetricConfigRepository.findByViewIdAndPosition(viewId, request.position())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            ViewMetricConfig.class.getSimpleName()
                    ));

            Integer temp = viewMetricConfig.getPosition();

            // leave existing position unoccupied
            existingPosition.setPosition((int) (totalMetrics + 1));
            viewMetricConfigRepository.flush();

            // swap positions
            viewMetricConfig.setPosition(request.position());
            viewMetricConfigRepository.flush();

            existingPosition.setPosition(temp);
            viewMetricConfigRepository.flush();
        }

        return new MetricResponse(
                metric.getId(),
                metric.getName(),
                metric.getDescription()
        );
    }

    @Transactional
    public void deleteMetric(Long id, Long leaderboardId) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));

        //base view: temp
        View view = viewRepository.findByLeaderboardIdAndBaseTrue(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName()
                ));

        Long viewId = view.getId();

        ViewMetricConfig viewMetricConfig = viewMetricConfigRepository.findByViewIdAndMetricId(viewId, id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ViewMetricConfig.class.getSimpleName()
                ));

        Integer deletedPosition = viewMetricConfig.getPosition();

        long totalMetrics = viewMetricConfigRepository.countByViewId(viewId);

        metricRepository.delete(metric);
        metricRepository.flush();

        //reorder positions
        int offset = Math.toIntExact(totalMetrics);
        //bypass unique constraint (viewId,position)
        viewMetricConfigRepository.movePositionsTemporarily(viewId, deletedPosition, offset);
        viewMetricConfigRepository.closePositionGap(viewId, offset);
    }
}
