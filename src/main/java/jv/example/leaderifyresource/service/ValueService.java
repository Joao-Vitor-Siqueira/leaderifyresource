package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.record.request.ValueRequest;
import jv.example.leaderifyresource.record.response.ValueResponse;
import jv.example.leaderifyresource.repository.EntryRepository;
import jv.example.leaderifyresource.repository.MetricRepository;
import jv.example.leaderifyresource.repository.ValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValueService {
    private final ValueRepository valueRepository;
    private final MetricRepository metricRepository;
    private final EntryRepository entryRepository;

    @Transactional
    public ValueResponse createValue(ValueRequest request) {
        Metric metric = metricRepository.findById(request.metricId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Metric.class.getSimpleName()
                ));
        Entry entry = entryRepository.findById(request.entryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Entry.class.getSimpleName()
                ));

        Optional<Value> exists =
                valueRepository.findByMetricIdAndEntryId(request.metricId(), request.entryId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException("Value already exists");
        }
        Value value = new Value();
        value.setEntry(entry);
        value.setMetric(metric);
        value.setValue(request.value());
        Value savedValue = valueRepository.save(value);

        return new ValueResponse(
                savedValue.getId(),
                metric.getId(),
                entry.getId(),
                savedValue.getValue()
        );
    }

    public ValueResponse findValueById(Long id) {
        Value value =
                valueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                        Value.class.getSimpleName()
                ));
        return new ValueResponse(
                value.getId(),
                value.getMetric().getId(),
                value.getEntry().getId(),
                value.getValue()
        );
    }

    public Page<ValueResponse> listValues(Pageable pageable) {
        return valueRepository.findAll(pageable)
                .map(value -> new ValueResponse(
                        value.getId(),
                        value.getMetric().getId(),
                        value.getEntry().getId(),
                        value.getValue()
                ));
    }

    @Transactional
    public ValueResponse updateValue(Long id, ValueRequest request) {
        Value value = valueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Value.class.getSimpleName()
                ));

        value.setValue(request.value());

        return new ValueResponse(
                value.getId(),
                value.getMetric().getId(),
                value.getEntry().getId(),
                value.getValue()
        );
    }

    @Transactional
    public void deleteValue(Long id) {
        Value value = valueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Value.class.getSimpleName()
                ));
        valueRepository.delete(value);
    }
}
