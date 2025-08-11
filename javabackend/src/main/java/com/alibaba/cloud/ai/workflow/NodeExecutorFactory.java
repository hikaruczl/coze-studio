package com.alibaba.cloud.ai.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NodeExecutorFactory {

    private final Map<String, NodeExecutor> executorMap;

    @Autowired
    public NodeExecutorFactory(List<NodeExecutor> executors) {
        this.executorMap = executors.stream()
                .collect(Collectors.toMap(NodeExecutor::getNodeType, Function.identity()));
    }

    public NodeExecutor getExecutor(String nodeType) {
        NodeExecutor executor = executorMap.get(nodeType);
        if (executor == null) {
            throw new IllegalArgumentException("No NodeExecutor found for type: " + nodeType);
        }
        return executor;
    }
}
