package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import com.alibaba.cloud.ai.service.PluginExecutionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("PLUGIN")
public class PluginNodeExecutor implements NodeExecutor {

    @Autowired
    private PluginExecutionService pluginExecutionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public WorkflowExecutionContext execute(WorkflowExecutionContext context, NodeDto node) {
        System.out.println("Executing Plugin Node: " + node.getName());

        Map<String, Object> nodeData = node.getData();
        if (nodeData == null || !nodeData.containsKey("pluginId") || !nodeData.containsKey("operationId") || !nodeData.containsKey("parameters")) {
            throw new IllegalStateException("Plugin node requires 'pluginId', 'operationId', and 'parameters' inputs.");
        }

        Long pluginId = Long.valueOf(resolveVariable(nodeData.get("pluginId").toString(), context));
        String operationId = resolveVariable(nodeData.get("operationId").toString(), context);
        String paramsJson = resolveVariable(nodeData.get("parameters").toString(), context);

        try {
            Map<String, Object> parameters = objectMapper.readValue(paramsJson, new TypeReference<>() {});
            String result = pluginExecutionService.executeTool(pluginId, operationId, parameters);

            String outputVariableName = node.getId() + ".output";
            context.addVariable(outputVariableName, result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute plugin node " + node.getId(), e);
        }

        return context;
    }
}
