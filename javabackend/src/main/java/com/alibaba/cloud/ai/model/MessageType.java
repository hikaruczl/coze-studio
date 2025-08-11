package com.alibaba.cloud.ai.model;

public enum MessageType {
    ACK,
    QUESTION,
    FUNCTION_CALL,
    TOOL_RESPONSE,
    KNOWLEDGE,
    ANSWER,
    FOLLOW_UP,
    INTERRUPT,
    VERBOSE
}
