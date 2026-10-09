package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
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
import jv.example.leaderifyresource.utils.Role;
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
    public ViewMetricConfigResponse createViewMetricConfig(ViewMetricConfigRequest request, Long viewId, Long metricId) {
        View view =
                viewRepository.findById(viewId).orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName()
                ));

        Role userRole = view.getUserLeaderboard().getRole();
        if(userRole.equals(Role.VIEWER))
            throw new AccessDeniedException(View.class.getSimpleName());

        Metric metric = metricRepository.findById(metricId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));

        Optional<ViewMetricConfig> exists =
                viewMetricConfigRepository.findByViewIdAndMetricId(viewId, metricId);
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException("ViewMetricConfig already exists");
        }

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
                metricId,
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
                        ViewMetricConfig.class.getSimpleName()
                ));
        return new ViewMetricConfigResponse(
                viewMetricConfig.getId(),
                viewMetricConfig.getMetric().getId(),
                viewMetricConfig.getVisibility(),
                viewMetricConfig.getSort(),
                viewMetricConfig.getNumericFilter(),
                viewMetricConfig.getTarget(),
                viewMetricConfig.getPosition()
        );
    }

    public Page<ViewMetricConfigResponse> listViewMetricConfigs(Long viewId,Pageable pageable) {
        return viewMetricConfigRepository.findAllByViewId(viewId,pageable)
                .map(viewMetricConfig -> new ViewMetricConfigResponse(
                        viewMetricConfig.getId(),
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
                        ViewMetricConfig.class.getSimpleName()
                ));

        viewMetricConfig.setVisibility(request.visibility());
        viewMetricConfig.setSort(request.sort());
        viewMetricConfig.setNumericFilter(request.numericFilter());
        viewMetricConfig.setTarget(request.target());
        viewMetricConfig.setPosition(request.position());

        return new ViewMetricConfigResponse(
                viewMetricConfig.getId(),
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
                        ViewMetricConfig.class.getSimpleName()
                ));
        viewMetricConfigRepository.delete(viewMetricConfig);
    }
}
