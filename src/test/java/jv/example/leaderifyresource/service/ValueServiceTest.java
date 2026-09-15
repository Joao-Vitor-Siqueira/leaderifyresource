package jv.example.leaderifyresource.service;

import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.record.request.ValueRequest;
import jv.example.leaderifyresource.record.response.ValueResponse;
import jv.example.leaderifyresource.repository.EntryRepository;
import jv.example.leaderifyresource.repository.MetricRepository;
import jv.example.leaderifyresource.repository.ValueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValueServiceTest {

    @InjectMocks
    private ValueService valueService;

    @Mock
    private ValueRepository valueRepository;

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private EntryRepository entryRepository;

    @Test
    void shouldSaveValue(){
        ValueRequest request = new ValueRequest(1L,1L,35.00);

        Metric metric = new Metric();
        metric.setId(1L);

        Entry entry = new Entry();
        entry.setId(1L);

        Value savedValue = new Value();
        savedValue.setId(1L);
        savedValue.setMetric(metric);
        savedValue.setEntry(entry);
        savedValue.setValue(35.00);

        when(metricRepository.findById(1L))
                .thenReturn(Optional.of(metric));

        when(entryRepository.findById(1L))
                .thenReturn(Optional.of(entry));

        when(valueRepository.findByMetricIdAndEntryId(1L, 1L))
                .thenReturn(Optional.empty());

        when(valueRepository.save(any(Value.class)))
                .thenReturn(savedValue);

        ValueResponse response = valueService.createValue(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.metricId());
        assertEquals(1L, response.entryId());
        assertEquals(35.00, response.value());

        verify(valueRepository).save(any(Value.class));
    }
}
