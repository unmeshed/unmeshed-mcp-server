package io.unmeshed.mcp;

import io.unmeshed.mcp.service.UnmeshedMCPService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UnmeshedMCPServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnmeshedMCPServerApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider unmeshedTools(UnmeshedMCPService unmeshedMCPService) {
		return MethodToolCallbackProvider.builder().toolObjects(unmeshedMCPService).build();
	}

}
