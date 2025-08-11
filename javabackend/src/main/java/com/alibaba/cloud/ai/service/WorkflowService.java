package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.dto.CanvasDto;
import com.alibaba.cloud.ai.dto.EdgeDto;
import com.alibaba.cloud.ai.dto.NodeDto;
import com.alibaba.cloud.ai.dto.SaveWorkflowRequest;
import com.alibaba.cloud.ai.model.Workflow;
import com.alibaba.cloud.ai.repository.WorkflowRepository;
import com.alibaba.cloud.ai.workflow.NodeExecutor;
import com.alibaba.cloud.ai.workflow.NodeExecutorFactory;
import com.alibaba.cloud.ai.workflow.StartNodeExecutor;
import com.alibaba.cloud.ai.workflow.WorkflowExecutionContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final ObjectMapper objectMapper;
    private final NodeExecutorFactory nodeExecutorFactory;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository, ObjectMapper objectMapper, NodeExecutorFactory nodeExecutorFactory) {
        this.workflowRepository = workflowRepository;
        this.objectMapper = objectMapper;
        this.nodeExecutorFactory = nodeExecutorFactory;
    }

    @Transactional
    public Workflow saveWorkflow(SaveWorkflowRequest request) {
        Workflow workflow;
        if (request.getId() != null) {
            workflow = workflowRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Workflow not found with id: " + request.getId()));
        } else {
            workflow = new Workflow();
            workflow.setCreatedAt(LocalDateTime.now());
        }

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());
        workflow.setUpdatedAt(LocalDateTime.now());

        try {
            workflow.setCanvas(objectMapper.writeValueAsString(request.getCanvas()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize canvas JSON", e);
        }

        return workflowRepository.save(workflow);
    }

    @Transactional(readOnly = true)
    public Map<String, JsonNode> runWorkflow(Long workflowId, Map<String, JsonNode> initialInputs) throws JsonProcessingException {
        // 1. Fetch and parse the workflow
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found with id: " + workflowId));
        CanvasDto canvas = objectMapper.readValue(workflow.getCanvas(), CanvasDto.class);

        // 2. Initialize context and find the start node
        WorkflowExecutionContext context = new WorkflowExecutionContext();
        NodeDto startNode = canvas.getNodes().stream()
                .filter(n -> StartNodeExecutor.NODE_TYPE.equals(n.getType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Workflow has no start node"));

        context.addNodeOutput(startNode.getId(), initialInputs);

        // 3. Simple sequential execution loop
        NodeDto currentNode = startNode;
        while (currentNode != null) {
            NodeExecutor executor = nodeExecutorFactory.getExecutor(currentNode.getType());
            Map<String, JsonNode> outputs = executor.execute(currentNode, context);
            context.addNodeOutput(currentNode.getId(), outputs);

            // Find the next node in the sequence.
            // This is a simplification and only supports linear workflows.
            final String currentId = currentNode.getId();
            Optional<EdgeDto> nextEdge = canvas.getEdges().stream()
                    .filter(edge -> edge.getSourceNodeId().equals(currentId))
                    .findFirst();

            if (nextEdge.isPresent()) {
                String nextNodeId = nextEdge.get().getTargetNodeId();
                currentNode = canvas.getNodes().stream()
                        .filter(n -> n.getId().equals(nextNodeId))
                        .findFirst()
                        .orElse(null);
            } else {
                currentNode = null; // End of workflow
            }
        }

        return context.getAllOutputs();
    }
}
