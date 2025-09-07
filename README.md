# Unmeshed MCP Server

A Spring Boot application that provides Model Context Protocol (MCP) server capabilities for interacting with the Unmeshed platform. This service allows AI agents (like Claude) to start and manage unmeshed processes through authenticated HTTP requests.

## 📋 Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [MCP Integration](#mcp-integration)
- [API Reference](#api-reference)
- [Development](#development)
- [Troubleshooting](#troubleshooting)

## ✨ Features

- **MCP Server**: Exposes unmeshed process management as MCP tools
- **HTTP Client**: Authenticated communication with Unmeshed API
- **JSON Configuration**: External configuration via command-line arguments
- **Bearer Token Authentication**: Secure API authentication with hashed tokens
- **Spring Boot**: Production-ready service with comprehensive HTTP support

## 🔧 Prerequisites

- **Java 21**
- **Gradle 8+** for building
- **Unmeshed Platform Access**: Valid client ID and auth token
- **MCP Client**: Such as Claude Desktop, Cursor or custom MCP implementation

## 📦 Installation

### 1. Clone Repository
```bash
git clone https://github.com/unmeshed/unmeshed-mcp-server
cd unmeshed-mcp-server
```

### 2. Build Application
```bash
./gradlew clean build
```

### 3. Verify Build
```bash
ls build/unmeshed-mcp-server-0.0.1-SNAPSHOT.jar
```


## 🔌 MCP Integration

### 1. Claude Desktop Integration

Edit `claude_desktop_config.json` (See Anthropic Claude docs for this):

```json
{
  "mcpServers": {
    "unmeshed-mcp-server": {
      "command": "bash",
      "args": [
        "-c",
        "java -jar /rootPath/unmeshed-mcp-server/build/libs/unmeshed-mcp-server-0.0.1-SNAPSHOT.jar"
      ],
      "env": {
        "UNMESHED_CLIENT_ID": "<UnmeshedClientId>",
        "UNMESHED_AUTH_TOKEN": "<<UnmeshedAuthToken>>",
        "UNMESHED_SERVER_URL": "<UnmeshedServerUrl>"
      }
    }
  }
}
```

### 3. Available MCP Tools

| Tool Name | Description | Parameters                                                       |
|-----------|-------------|------------------------------------------------------------------|
| `startUnmeshedProcessAsynchronouslyByName` | Start a new unmeshed process in Asynchronous mode | `name` `namespace` `version` `input` `requestId` `correlationId` |
| `startUnmeshedProcessAsynchronouslyByName` | Start a new unmeshed process in Synchronous mode  | `name` `namespace` `version` `input` `requestId` `correlationId` |
| `invokeUnmeshedApiMappingPost`  | Invoke Unmeshed Post Api Mappings (Webhooks)      | `name` `namespace` `version` `input` `requestId` `correlationId` |


### Adding New Tools

1. **Create tool method in `UnmeshedMCPService`:**
```java
@Tool(description = "Description of what this tool does")
public String myNewTool(MyRequestModel request) {
    // Use UnmeshedSDK to make call
    return response;
}
```



### Common Issues

**1. Missing credentials:**
```
Error: UNMESHED_CLIENT_ID and UNMESHED_AUTH_TOKEN are required
```
- ✅ Check JSON syntax in config file
- ✅ Verify field names match exactly: `UNMESHED_CLIENT_ID`, `UNMESHED_AUTH_TOKEN`
- ✅ Ensure values are strings, not null

**3. Authentication failed:**
```
HTTP 401 Unauthorized
```
- ✅ Verify client ID and auth token are correct
- ✅ Check if tokens have expired
- ✅ Ensure server URL is correct

**4. Connection refused:**
```
Connection refused: connect
```
- ✅ Verify `UNMESHED_SERVER_URL` is reachable
- ✅ Check network connectivity
- ✅ Verify server is running

**5. MCP client can't start server:**
```
Failed to start MCP server
```
- ✅ Use absolute paths in MCP config
- ✅ Verify Java is in PATH
- ✅ Check JAR file exists and is executable


## 📞 Support

- **Documentation**: Check this README and inline code documentation
- **Issues**: Create GitHub issues for bugs or feature requests
- **Configuration**: Validate JSON syntax using online JSON validators

## 🔄 Version History

- **v1.0.0**: Initial release with MCP server and HTTP client
- Authentication with Bearer tokens
- JSON-based configuration
- Full HTTP method support (GET, POST, PUT, DELETE, PATCH)

---

**⚠️ Security Note**: Keep your `UNMESHED_AUTH_TOKEN` secure and never commit configuration files with real credentials to version control.
