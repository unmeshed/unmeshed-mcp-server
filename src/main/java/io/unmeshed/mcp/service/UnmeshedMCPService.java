package io.unmeshed.mcp.service;

import io.unmeshed.api.common.ProcessData;
import io.unmeshed.api.common.ProcessRequestData;
import io.unmeshed.client.UnmeshedClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnmeshedMCPService {

    private final UnmeshedClient unmeshedClient;

    @Tool(
            description = "Starts a new Unmeshed process execution by name"
    )
    public ProcessData startUnmeshedProcessByName(
            @ToolParam(description = "The unique name of the process to start.") String name,
            @ToolParam(description = "The namespace under which the process should run. Defaults to 'default' if null.") String namespace,
            @ToolParam(description = "The version number of the process definition to use. If null, the latest version is used.") Integer version,
            @ToolParam(description = "An optional unique identifier for this specific request. Useful for tracking or retrying processes.") String requestId,
            @ToolParam(description = "An optional identifier to correlate this process execution with other related processes or events.") String correlationId,
            @ToolParam(description = "A map of input values for the process execution, with keys as parameter names and values as their corresponding values.") Map<String, Object> input) {

        ProcessRequestData request = ProcessRequestData.builder()
                .name(name)
                .namespace(namespace != null ? namespace : "default")
                .version(version)
                .requestId(requestId)
                .correlationId(correlationId)
                .input(input)
                .build();

        try {
            return unmeshedClient.runProcessAsync(request);
        } catch (Exception e) {
            return ProcessData.builder()
                    .output(Map.of("error", "Failed to start process: " + e.getMessage()))
                    .build();
        }
    }
}
