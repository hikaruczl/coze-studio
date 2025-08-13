package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("KNOWLEDGE")
public class KnowledgeNodeExecutor implements NodeExecutor {

    @Autowired
    private VectorStore vectorStore;

    @Override
    public WorkflowExecutionContext execute(WorkflowExecutionContext context, NodeDto node) {
        System.out.println("Executing Knowledge Node: " + node.getName());

        // Assuming node inputs are in the 'data' map of NodeDto
        Map<String, Object> nodeData = node.getData();
        if (nodeData == null || !nodeData.containsKey("query")) {
            throw new IllegalStateException("Knowledge node requires a 'query' input.");
        }

        // The query can be a direct value or a reference to a context variable like {{start.query}}
        String query = resolveVariable(nodeData.get("query").toString(), context);

        // Perform the similarity search
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(query).withTopK(2));
        String documentsContent = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        // Add the result to the context
        String outputVariableName = node.getId() + ".output";
        context.addVariable(outputVariableName, documentsContent);

        return context;
    }
}
