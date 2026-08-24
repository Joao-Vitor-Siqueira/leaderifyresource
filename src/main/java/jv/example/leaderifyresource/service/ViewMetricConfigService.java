package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.View;
import jv.example.leaderifyresource.model.ViewMetricConfig;
import jv.example.leaderifyresource.record.request.ViewMetricConfigRequest;
import jv.example.leaderifyresource.record.response.ViewMetricConfigResponse;
import jv.example.leaderifyresource.repository.MetricRepository;
import jv.example.leaderifyresource.repository.ViewMetricConfigRepository;
import jv.example.leaderifyresource.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewMetricConfigService {
    private final ViewMetricConfigRepository viewMetricConfigRepository;
    private final ViewRepository viewRepository;
    private final MetricRepository metricRepository;

    @Transactional
    public ViewMetricConfigResponse createViewMetricConfig(ViewMetricConfigRequest request) {
        Optional<ViewMetricConfig> exists =
                viewMetricConfigRepository.findByViewIdAndMetricId(request.viewId(), request.metricId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(ViewMetricConfig.class.getSimpleName(), exists.get().getId());
        }
        View view =
                viewRepository.findById(request.viewId()).orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName(), request.viewId()
                ));
        Metric metric = metricRepository.findById(request.metricId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName(), request.metricId()
                ));

        ViewMetricConfig viewMetricConfig = new ViewMetricConfig();
        viewMetricConfig.setView(view);
        viewMetricConfig.setMetric(metric);
        viewMetricConfig.setVisibility(request.visibility());
        viewMetricConfig.setSort(request.sort());
        viewMetricConfig.setNumericFilter(request.numericFilter());
        viewMetricConfig.setTarget(request.target());
        viewMetricConfig.setPosition(request.position());

        ViewMetricConfig savedViewMetricConfig = viewMetricConfigRepository.save(viewMetricConfig);

        return new ViewMetricConfigResponse(
                savedViewMetricConfig.getId(),
                view.getId(),
                metric.getId(),
                viewMetricConfig.getVisibility(),
                viewMetricConfig.getSort(),
                viewMetricConfig.getNumericFilter(),
                viewMetricConfig.getTarget(),
                viewMetricConfig.getPosition()
        );
    }

    public ViewMetricConfigResponse findViewMetricConfigById(Long id) {
        ViewMetricConfig viewMetricConfig =
                viewMetricConfigRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                        ViewMetricConfig.class.getSimpleName(), id
                ));
        return new ViewMetricConfigResponse(
                viewMetricConfig.getId(),
                viewMetricConfig.getView().getId(),
                viewMetricConfig.getMetric().getId(),
                viewMetricConfig.getVisibility(),
                viewMetricConfig.getSort(),
                viewMetricConfig.getNumericFilter(),
                viewMetricConfig.getTarget(),
                viewMetricConfig.getPosition()
        );
    }

    public Page<ViewMetricConfigResponse> listViewMetricConfigs(Pageable pageable) {
        return viewMetricConfigRepository.findAll(pageable)
                .map(viewMetricConfig -> new ViewMetricConfigResponse(
                        viewMetricConfig.getId(),
                        viewMetricConfig.getView().getId(),
                        viewMetricConfig.getMetric().getId(),
                        viewMetricConfig.getVisibility(),
                        viewMetricConfig.getSort(),
                        viewMetricConfig.getNumericFilter(),
                        viewMetricConfig.getTarget(),
                        viewMetricConfig.getPosition()
                ));
    }

    @Transactional
    public ViewMetricConfigResponse updateViewMetricConfig(Long id, ViewMetricConfigRequest request) {
        ViewMetricConfig viewMetricConfig = viewMetricConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ViewMetricConfig.class.getSimpleName(), id
                ));

        viewMetricConfig.setVisibility(request.visibility());
        viewMetricConfig.setSort(request.sort());
        viewMetricConfig.setNumericFilter(request.numericFilter());
        viewMetricConfig.setTarget(request.target());
        viewMetricConfig.setPosition(request.position());

        return new ViewMetricConfigResponse(
                viewMetricConfig.getId(),
                viewMetricConfig.getView().getId(),
                viewMetricConfig.getMetric().getId(),
                viewMetricConfig.getVisibility(),
                viewMetricConfig.getSort(),
                viewMetricConfig.getNumericFilter(),
                viewMetricConfig.getTarget(),
                viewMetricConfig.getPosition()
        );
    }

    @Transactional
    public void deleteViewMetricConfig(Long id) {
        ViewMetricConfig viewMetricConfig = viewMetricConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ViewMetricConfig.class.getSimpleName(), id
                ));
        viewMetricConfigRepository.delete(viewMetricConfig);
    }
}
