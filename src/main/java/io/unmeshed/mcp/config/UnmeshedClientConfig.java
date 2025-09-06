package io.unmeshed.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.unmeshed.client.ClientConfig;
import io.unmeshed.client.UnmeshedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnmeshedClientConfig {

    private final String unmeshedClientId;
    private final String unmeshedAuthToken;
    private final String unmeshedServerUrl;

    public UnmeshedClientConfig(
            @Value("${UNMESHED_CLIENT_ID}") String unmeshedClientId,
            @Value("${UNMESHED_AUTH_TOKEN}") String unmeshedAuthToken,
            @Value("${UNMESHED_SERVER_URL}") String unmeshedServerUrl
    ) {
        this.unmeshedClientId = unmeshedClientId;
        this.unmeshedAuthToken = unmeshedAuthToken;
        this.unmeshedServerUrl = unmeshedServerUrl;
        if (unmeshedClientId == null || unmeshedAuthToken == null || unmeshedServerUrl == null) {
            throw new RuntimeException("[UNMESHED_CLIENT_ID], [UNMESHED_AUTH_TOKEN] and [UNMESHED_SERVER_URL] are mandatory parameters");
        }
    }

    @Bean
    public UnmeshedClient unmeshedClient() {
        ClientConfig.ClientConfigBuilder builder = ClientConfig
                .builder(unmeshedClientId, unmeshedAuthToken)
                .baseUrl(unmeshedServerUrl)
                .workRequestBatchSize(10)
                .initialDelayMillis(20)
                .stepTimeoutMillis(Long.MAX_VALUE);

        if (unmeshedServerUrl != null && unmeshedServerUrl.contains("localhost")) {
            builder.port(8080);
        }

        return new UnmeshedClient(builder.build());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
