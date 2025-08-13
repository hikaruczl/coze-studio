package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import org.springframework.stereotype.Component;

@Component("START")
public class StartNodeExecutor implements NodeExecutor {

    @Override
    public WorkflowExecutionContext execute(WorkflowExecutionContext context, NodeDto node) {
        System.out.println("Executing Start Node: " + node.getName());
        // The start node's main job is to take the initial workflow inputs
        // and place them into the execution context. The WorkflowService handles this
        // before the first node is executed. So, this executor doesn't need to do much.
        // It just passes the context along.
        // We can add validation here in the future.
        return context;
    }
}
