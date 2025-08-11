package com.example.javabackend.service;

import com.example.javabackend.controller.dto.RegisterPluginRequest;
import com.example.javabackend.model.Plugin;
import com.example.javabackend.repository.PluginRepository;
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
        } else {
             plugin.setName(request.getName());
             plugin.setDescription(request.getDescription());
        }

        plugin.setType(request.getPluginType());
        plugin.setOpenapi(request.getOpenapi());
        // Set other fields from request as needed
        return pluginRepository.save(plugin);
    }

    public Optional<Plugin> getPluginInfo(Long pluginId) {
        return pluginRepository.findById(pluginId);
    }
}
