package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.dto.CanvasDto;
import com.alibaba.cloud.ai.dto.EdgeDto;
import com.alibaba.cloud.ai.dto.NodeDto;
import com.alibaba.cloud.ai.workflow.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkflowServiceTest {

    @Mock
    private NodeExecutorFactory nodeExecutorFactory;

    @Mock
    private StartNodeExecutor startNodeExecutor;

    @Mock
    private EndNodeExecutor endNodeExecutor;

    @InjectMocks
    private WorkflowService workflowService;

    private CanvasDto simpleWorkflow;

    @BeforeEach
    void setUp() {
        NodeDto startNode = new NodeDto();
        startNode.setId("start-node");
        startNode.setType("START");
        startNode.setName("Start");

        NodeDto endNode = new NodeDto();
        endNode.setId("end-node");
        endNode.setType("END");
        endNode.setName("End");

        EdgeDto edge = new EdgeDto();
        edge.setId("edge-1");
        edge.setSource("start-node");
        edge.setTarget("end-node");

        simpleWorkflow = new CanvasDto();
        simpleWorkflow.setNodes(List.of(startNode, endNode));
        simpleWorkflow.setEdges(List.of(edge));
    }

    @Test
    void testRunWorkflow_simpleStartToEnd() {
        // Arrange
        when(nodeExecutorFactory.getExecutor("START")).thenReturn(startNodeExecutor);
        when(nodeExecutorFactory.getExecutor("END")).thenReturn(endNodeExecutor);

        // Mock the behavior of the executors
        when(startNodeExecutor.execute(any(WorkflowExecutionContext.class), any(NodeDto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Pass context through

        when(endNodeExecutor.execute(any(WorkflowExecutionContext.class), any(NodeDto.class)))
                .thenAnswer(invocation -> {
                    WorkflowExecutionContext ctx = invocation.getArgument(0);
                    ctx.setFinished(true);
                    ctx.addVariable("finalResult", "Workflow executed successfully!");
                    return ctx;
                });

        // Act
        Map<String, Object> initialParams = Collections.singletonMap("query", "test query");
        WorkflowExecutionContext finalContext = workflowService.runWorkflow(simpleWorkflow, initialParams);

        // Assert
        assertTrue(finalContext.isFinished());
        assertEquals("Workflow executed successfully!", finalContext.getVariable("finalResult"));
    }
}
