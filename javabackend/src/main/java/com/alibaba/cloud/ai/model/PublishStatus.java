package com.alibaba.cloud.ai.model;

public enum PublishStatus {
    PUBLISH_STATUS_UNSPECIFIED(0),
    PUBLISH_STATUS_OF_PACK_DOING(1),
    PUBLISH_STATUS_OF_PACK_DONE(2),
    PUBLISH_STATUS_OF_PUBLISH_DOING(3),
    PUBLISH_STATUS_OF_PUBLISH_DONE(4),
    PUBLISH_STATUS_OF_PACK_FAILED(5),
    PUBLISH_STATUS_OF_PUBLISH_FAILED(6);

    private final int value;

    PublishStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
