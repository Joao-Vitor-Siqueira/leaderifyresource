package jv.example.leaderifyresource.service;

import jv.example.leaderifyresource.client.ComputeClient;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Leaderboard;
import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.model.ViewMetricConfig;
import jv.example.leaderifyresource.record.EntryMetricValue;
import jv.example.leaderifyresource.record.MetricSort;
import jv.example.leaderifyresource.record.MetricValue;
import jv.example.leaderifyresource.record.request.CalculationRequest;
import jv.example.leaderifyresource.record.response.CalculationResponse;
import jv.example.leaderifyresource.repository.EntryRepository;
import jv.example.leaderifyresource.repository.LeaderboardRepository;
import jv.example.leaderifyresource.repository.ValueRepository;
import jv.example.leaderifyresource.repository.ViewMetricConfigRepository;
import jv.example.leaderifyresource.utils.Sort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ComputeService {
    private final ComputeClient computeClient;
    private final ViewMetricConfigRepository viewMetricConfigRepository;
    private final EntryRepository  entryRepository;
    private final ValueRepository valueRepository;
    private final LeaderboardRepository leaderboardRepository;


    public CalculationResponse calculate(Long leaderboardId,Long viewId){
        Leaderboard leaderboard = leaderboardRepository.findById(leaderboardId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Leaderboard.class.getSimpleName()
                ));

        List<ViewMetricConfig> viewMetricConfigs = viewMetricConfigRepository.findAllByViewId(viewId);
        if(viewMetricConfigs.isEmpty()){
            throw new ResourceNotFoundException(ViewMetricConfig.class.getSimpleName());
        }

        Map<Integer, MetricSort> metricSorts = new HashMap<>();
        Map<Integer, Long> positionMetricIds = new HashMap<>();

        for(ViewMetricConfig viewMetricConfig : viewMetricConfigs){
            Sort sort = viewMetricConfig.getSort();
            String metricName = viewMetricConfig.getMetric().getName();
            MetricSort metricSort = new MetricSort(metricName, sort);

            Integer position = viewMetricConfig.getPosition();
            metricSorts.put(position, metricSort);
            positionMetricIds.put(position, viewMetricConfig.getMetric().getId());
        }

        List<Entry> entries = entryRepository.findAllByLeaderboardId(leaderboardId);
        if(entries.isEmpty()){
            throw new ResourceNotFoundException(Entry.class.getSimpleName());
        }

        List<EntryMetricValue> entryMetricValues = new ArrayList<>();

        for(Entry entry : entries){
            List<MetricValue> scores = new ArrayList<>();
            String entryName = entry.getName();
            for (int i = 0; i < positionMetricIds.size(); i++) {
                Long metricId = positionMetricIds.get(i);
                Value value = valueRepository.findByMetricIdAndEntryId(metricId, entry.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(Value.class.getSimpleName()));
                String metricName = value.getMetric().getName();
                scores.add(new MetricValue(metricName , value.getValue()));
            }
            entryMetricValues.add(new EntryMetricValue(entryName, scores));
        }

        return computeClient.calculate(
                new CalculationRequest(
                        leaderboard.getName(),
                        entryMetricValues,
                        metricSorts
                )
        );
    }
}
