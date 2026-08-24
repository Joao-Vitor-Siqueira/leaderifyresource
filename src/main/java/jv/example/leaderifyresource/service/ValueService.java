package jv.example.leaderifyresource.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.record.request.MetricRequest;
import jv.example.leaderifyresource.record.request.ValueRequest;
import jv.example.leaderifyresource.record.response.MetricResponse;
import jv.example.leaderifyresource.record.response.ValueResponse;
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

    @Transactional
    public ValueResponse createValue(ValueRequest request) {
        Optional<Value> exists =
                valueRepository.findByMetricIdAndEntryId(request.metric().getId(), request.entry().getId());
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(Value.class.getSimpleName(), exists.get().getId());
        }
        Value value = new Value();
        value.setEntry(request.entry());
        value.setMetric(request.metric());
        value.setValue(request.value());
        Value savedValue = valueRepository.save(value);

        return new ValueResponse(
                savedValue.getId(),
                savedValue.getMetric().getId(),
                savedValue.getEntry().getId(),
                savedValue.getValue()
        );
    }

    public ValueResponse findValueById(Long id) {
        return valueRepository.findValueResponseById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                Value.class.getSimpleName(), id));
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
                        Value.class.getSimpleName(), id
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
                        Value.class.getSimpleName(), id
                ));
        valueRepository.delete(value);
    }
}
