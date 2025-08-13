package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.dto.RegisterPluginRequest;
import com.alibaba.cloud.ai.model.Plugin;
import com.alibaba.cloud.ai.repository.PluginRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PluginService {

    @Autowired
    private PluginRepository pluginRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Plugin registerPlugin(RegisterPluginRequest request) throws JsonProcessingException {
        Plugin plugin = new Plugin();

        if (request.getAiPlugin() != null && !request.getAiPlugin().isEmpty()) {
            JsonNode aiPluginNode = objectMapper.readTree(request.getAiPlugin());
            if (aiPluginNode.has("name_for_model")) {
                plugin.setName(aiPluginNode.get("name_for_model").asText());
            }
            if (aiPluginNode.has("description_for_model")) {
                plugin.setDescription(aiPluginNode.get("description_for_model").asText());
            }
        }

        plugin.setType(request.getPluginType());
        plugin.setOpenapi(request.getOpenapi());
        // Set other fields from request as needed
        return pluginRepository.save(plugin);
    }

    public Optional<Plugin> getPluginInfo(Long pluginId) {
        return pluginRepository.findById(pluginId);
    }

    public Optional<Plugin> updatePlugin(Long pluginId, UpdatePluginRequest request) {
        return pluginRepository.findById(pluginId).map(plugin -> {
            if (request.getName() != null) {
                plugin.setName(request.getName());
            }
            if (request.getDescription() != null) {
                plugin.setDescription(request.getDescription());
            }
            if (request.getType() != null) {
                plugin.setType(request.getType());
            }
            if (request.getOpenapiDoc() != null) {
                plugin.setOpenapiDoc(request.getOpenapiDoc());
            }
            return pluginRepository.save(plugin);
        });
    }

    public void deletePlugin(Long pluginId) {
        pluginRepository.deleteById(pluginId);
    }

    public Optional<Plugin> publishPlugin(Long pluginId) {
        return pluginRepository.findById(pluginId).map(plugin -> {
            plugin.setPublished(true);
            return pluginRepository.save(plugin);
        });
    }
}
