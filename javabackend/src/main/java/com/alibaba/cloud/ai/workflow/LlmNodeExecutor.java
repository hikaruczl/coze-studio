package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import com.alibaba.cloud.ai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("LLM")
public class LlmNodeExecutor implements NodeExecutor {

    @Autowired
    private ChatService chatService;

    @Override
    public WorkflowExecutionContext execute(WorkflowExecutionContext context, NodeDto node) {
        System.out.println("Executing LLM Node: " + node.getName());

        Map<String, Object> nodeData = node.getData();
        if (nodeData == null || !nodeData.containsKey("prompt")) {
            throw new IllegalStateException("LLM node requires a 'prompt' input.");
        }

        Long botId = Long.valueOf(resolveVariable(nodeData.get("botId").toString(), context));
        String prompt = resolveVariable(nodeData.get("prompt").toString(), context);

        String result = chatService.chat(botId, prompt);

        String outputVariableName = node.getId() + ".output";
        context.addVariable(outputVariableName, result);

        return context;
    }
}
