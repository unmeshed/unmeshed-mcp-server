package io.unmeshed.mcp.service;

import io.unmeshed.api.common.ProcessData;
import io.unmeshed.api.common.ProcessRequestData;
import io.unmeshed.client.UnmeshedClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnmeshedMCPService {


    private final UnmeshedClient unmeshedClient;

    @Tool(
            description = "Starts a new Unmeshed process execution by name.\n" +
                    "Parameters:\n" +
                    "- name: The unique name of the process to start.\n" +
                    "- namespace: The namespace under which the process should run. Defaults to 'default' if null.\n" +
                    "- version: The version number of the process definition to use. If null, the latest version is used.\n" +
                    "- requestId: An optional unique identifier for this specific request. Useful for tracking or retrying processes.\n" +
                    "- correlationId: An optional identifier to correlate this process execution with other related processes or events.\n" +
                    "- input: A map of input values for the process execution, with keys as parameter names and values as their corresponding values."
    )
    public ProcessData startUnmeshedProcessByName(
            String name,
            String namespace,
            Integer version,
            String requestId,
            String correlationId,
            Map<String, Object> input) {

        ProcessRequestData request = ProcessRequestData.builder()
                .name(name)
                .namespace(namespace != null ? namespace : "default")
                .version(version)
                .requestId(requestId)
                .correlationId(correlationId)
                .input(input)
                .build();

        try {
            return unmeshedClient.runProcessAsync(processRequestData);
        } catch (Exception e) {
            return ProcessData.builder()
                    .output(Map.of("error", "Failed to start process: " + e.getMessage()))
                    .build();
        }
    }
}
