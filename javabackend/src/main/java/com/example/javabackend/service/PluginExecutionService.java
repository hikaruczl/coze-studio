package com.example.javabackend.service;

import com.example.javabackend.model.Plugin;
import com.example.javabackend.repository.PluginRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
public class PluginExecutionService {

    @Autowired
    private PluginRepository pluginRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String executeTool(Long pluginId, String operationId, Map<String, Object> parameters) throws Exception {
        Plugin plugin = pluginRepository.findById(pluginId)
                .orElseThrow(() -> new IllegalArgumentException("Plugin not found with id: " + pluginId));

        OpenAPI openAPI = new OpenAPIV3Parser().readContents(plugin.getOpenapi()).getOpenAPI();
        if (openAPI == null) {
            throw new Exception("Failed to parse OpenAPI spec for plugin: " + pluginId);
        }

        // Find the operation by operationId
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String pathUrl = entry.getKey();
            PathItem pathItem = entry.getValue();

            for (Map.Entry<PathItem.HttpMethod, Operation> opEntry : pathItem.getOperations().entrySet()) {
                if (opEntry.getValue().getOperationId().equals(operationId)) {
                    PathItem.HttpMethod method = opEntry.getKey();

                    String baseUrl = openAPI.getServers().get(0).getUrl();
                    String fullUrl = baseUrl + pathUrl;

                    // Replace path parameters
                    for (Map.Entry<String, Object> param : parameters.entrySet()) {
                        if (fullUrl.contains("{" + param.getKey() + "}")) {
                            fullUrl = fullUrl.replace("{" + param.getKey() + "}", String.valueOf(param.getValue()));
                        }
                    }

                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    HttpEntity<Map<String, Object>> entity;
                    ResponseEntity<String> response;

                    if (method == PathItem.HttpMethod.POST) {
                         headers.setContentType(MediaType.APPLICATION_JSON);
                         entity = new HttpEntity<>(parameters, headers);
                         response = restTemplate.postForEntity(fullUrl, entity, String.class);
                    } else { // Assume GET
                         UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fullUrl);
                         for(Map.Entry<String, Object> param : parameters.entrySet()){
                             // A simple check to avoid adding path params as query params
                             if(!pathUrl.contains("{" + param.getKey() + "}")) {
                                builder.queryParam(param.getKey(), param.getValue());
                             }
                         }
                         entity = new HttpEntity<>(headers);
                         response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
                    }

                    return response.getBody();
                }
            }
        }

        throw new IllegalArgumentException("Operation '" + operationId + "' not found in plugin: " + pluginId);
    }
}
