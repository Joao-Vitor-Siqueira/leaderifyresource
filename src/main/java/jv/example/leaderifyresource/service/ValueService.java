package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.record.request.ValueBulkRequest;
import jv.example.leaderifyresource.record.request.ValueRequest;
import jv.example.leaderifyresource.record.response.ValueResponse;
import jv.example.leaderifyresource.repository.EntryRepository;
import jv.example.leaderifyresource.repository.MetricRepository;
import jv.example.leaderifyresource.repository.ValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<ValueResponse> bulkUpdateValues(List<ValueBulkRequest> requests) {
        List<Long> ids = requests.stream()
                .map(ValueBulkRequest::id)
                .toList();

        List<Value> values = valueRepository.findAllById(ids);
        List<ValueResponse> responses = new ArrayList<>();

        if(values.size() == requests.size()) {
            Map<Long, Value> valuesById = values.stream()
                    .collect(Collectors.toMap(Value::getId, Function.identity()));

            for (ValueBulkRequest request : requests) {
                Value value = valuesById.get(request.id());
                value.setValue(request.value());
                responses.add(new ValueResponse(value.getId(), value.getMetric().getId(), value.getEntry().getId(), value.getValue()));
            }
        }
        return responses;
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
