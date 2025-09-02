package com.coze.studio.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * 更新文档请求
 * 
 * @author coze-dev
 */
@Data
@Schema(description = "更新文档请求")
public class UpdateDocumentRequest {

    @Size(max = 200, message = "文档标题长度不能超过200个字符")
    @Schema(description = "文档标题", example = "技术文档")
    private String title;

    @Size(max = 1000, message = "描述长度不能超过1000个字符")
    @Schema(description = "文档描述", example = "这是一个技术文档")
    private String description;

    @Schema(description = "标签列表", example = "[\"技术\", \"文档\"]")
    private String[] tags;

    @Schema(description = "文档状态", example = "ACTIVE")
    private String status;

    @Schema(description = "元数据")
    private Map<String, Object> metadata;

    @Schema(description = "是否公开", example = "false")
    private Boolean isPublic;
}
