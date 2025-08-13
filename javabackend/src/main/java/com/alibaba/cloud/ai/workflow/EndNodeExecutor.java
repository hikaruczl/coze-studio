package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import org.springframework.stereotype.Component;

@Component("END")
public class EndNodeExecutor implements NodeExecutor {

    @Override
    public WorkflowExecutionContext execute(WorkflowExecutionContext context, NodeDto node) {
        System.out.println("Executing End Node: " + node.getName());
        // The end node signals the completion of the workflow.
        // It can be used to format the final output of the workflow.
        // For now, it just marks the context as finished.
        context.setFinished(true);
        return context;
    }
}
