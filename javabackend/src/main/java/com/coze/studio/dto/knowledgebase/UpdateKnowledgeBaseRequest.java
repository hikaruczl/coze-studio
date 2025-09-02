package com.coze.studio.dto.knowledgebase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * 更新知识库请求
 * 
 * @author coze-dev
 */
@Data
@Schema(description = "更新知识库请求")
public class UpdateKnowledgeBaseRequest {

    @Size(max = 100, message = "知识库名称长度不能超过100个字符")
    @Schema(description = "知识库名称", example = "技术文档知识库")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Schema(description = "知识库描述", example = "存储技术文档和API说明")
    private String description;

    @Schema(description = "是否公开", example = "false")
    private Boolean isPublic;

    @Schema(description = "标签列表", example = "[\"技术\", \"文档\"]")
    private String[] tags;

    @Schema(description = "配置信息")
    private Map<String, Object> config;

    @Schema(description = "向量存储ID", example = "1")
    private Long vectorStoreId;

    @Schema(description = "状态", example = "ACTIVE")
    private String status;
}
