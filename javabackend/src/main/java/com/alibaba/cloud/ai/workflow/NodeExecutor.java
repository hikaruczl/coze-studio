package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface NodeExecutor {

    WorkflowExecutionContext execute(WorkflowExecutionContext context, NodeDto node);

    default String resolveVariable(String value, WorkflowExecutionContext context) {
        if (value == null || !value.contains("{{") || !value.contains("}}")) {
            return value;
        }

        Pattern pattern = Pattern.compile("\\{\\{([^}]+)\\}\\}");
        Matcher matcher = pattern.matcher(value);

        StringBuilder resolvedValue = new StringBuilder();
        while (matcher.find()) {
            String variablePath = matcher.group(1);
            Object variableValue = context.getVariable(variablePath);
            String replacement = (variableValue != null) ? variableValue.toString() : "";
            matcher.appendReplacement(resolvedValue, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(resolvedValue);

        return resolvedValue.toString();
    }
}
