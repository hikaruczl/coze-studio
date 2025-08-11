package com.alibaba.cloud.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebugApiResponse {
    private boolean success;
    private String resp;
    @JsonProperty("raw_resp")
    private String rawResp;
    @JsonProperty("raw_req")
    private String rawReq;
    private String reason;
}
