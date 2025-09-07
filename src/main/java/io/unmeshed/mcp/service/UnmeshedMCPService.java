package io.unmeshed.mcp.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.unmeshed.api.common.ApiCallType;
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
            description = "Starts a new Unmeshed process asynchronously using its unique name, without waiting for completion. " +
                    "This tool is useful when you need to trigger a process in the background using unmeshed orchestration engine and continue with other tasks. " +
                    "It supports specifying namespace, version, request ID, correlation ID, and input parameters. " +
                    "Returns process execution details or an error message if the process could not be started."
    )
    public ProcessData startUnmeshedProcessAsynchronouslyByName(
            @ToolParam(description = "The unique name of the process to start.") String name,
            @ToolParam(description = "The namespace under which the process should run. Defaults to 'default' if null.") String namespace,
            @ToolParam(description = "An optional version number of the process definition to use.") Integer version,
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

    @Tool(
            description = "Starts a new Unmeshed process synchronously using its unique name, waiting for the process to complete before returning. " +
                    "This tool is useful when you need the final output of a process before proceeding with other tasks. " +
                    "It uses the Unmeshed orchestration engine and supports specifying namespace, version, request ID, correlation ID, and input parameters. " +
                    "Returns the final process execution details or an error message if the process could not be started or completed."
    )
    public ProcessData startUnmeshedProcessSynchronouslyByName(
            @ToolParam(description = "The unique name of the process to start.") String name,
            @ToolParam(description = "The namespace under which the process should run. Defaults to 'default' if null.") String namespace,
            @ToolParam(description = "An optional version number of the process definition to use.") Integer version,
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
            return unmeshedClient.runProcessSync(request);
        } catch (Exception e) {
            return ProcessData.builder()
                    .output(Map.of("error", "Failed to start process: " + e.getMessage()))
                    .build();
        }
    }

    @Tool(
            description = "Invokes an API mapping through the Unmeshed orchestration engine using a POST request. " +
                    "This tool allows sending a payload to a specified endpoint, optionally including a request ID and correlation ID for tracking. " +
                    "The API call type can be specified to control execution behavior."
    )
    public JsonNode invokeUnmeshedApiMappingPost(
            @ToolParam(description = "The target API endpoint path or identifier to be invoked.") String endpoint,
            @ToolParam(description = "An optional unique identifier for this specific request, useful for tracking or retrying calls.") String requestId,
            @ToolParam(description = "An optional identifier to correlate this API call with related requests or processes.") String correlationId,
            @ToolParam(description = "The request payload as a map of key-value pairs to be sent to the endpoint.") Map<String, Object> inputPayload,
            @ToolParam(description = "Specifies the type of API call (e.g., SYNC, ASYNC, STREAM) that determines how the request is executed with keys as parameter names and values as their corresponding values.") ApiCallType apiCallType
    ) {
        return unmeshedClient.invokeApiMappingPost(endpoint, requestId, correlationId, inputPayload, apiCallType);
    }

}
