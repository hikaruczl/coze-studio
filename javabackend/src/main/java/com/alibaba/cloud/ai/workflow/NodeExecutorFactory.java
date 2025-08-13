package com.alibaba.cloud.ai.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NodeExecutorFactory {

    @Autowired
    private ApplicationContext context;

    public NodeExecutor getExecutor(String nodeType) {
        if (nodeType == null) {
            throw new IllegalArgumentException("Node type cannot be null");
        }
        // Assumes the bean name in the @Component annotation matches the node type from the canvas
        return context.getBean(nodeType.toUpperCase(), NodeExecutor.class);
    }
}
