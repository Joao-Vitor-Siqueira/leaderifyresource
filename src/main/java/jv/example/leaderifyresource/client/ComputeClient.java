package jv.example.leaderifyresource.client;

import jv.example.leaderifyresource.record.request.CalculationRequest;
import jv.example.leaderifyresource.record.response.CalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@Service
public class ComputeClient {

    private final RestClient restClient;
    private final String computeUrl;

    public ComputeClient(
            RestClient restClient,
            @Value("${compute-client.url}") String computeUrl) {
        this.restClient = restClient;
        this.computeUrl = computeUrl;
    }

    public CalculationResponse calculate(CalculationRequest calculationRequest) {
        return restClient.post()
                .uri(computeUrl)
                .attributes(clientRegistrationId("compute-client"))
                .body(calculationRequest)
                .retrieve()
                .body(CalculationResponse.class);
    }
}
