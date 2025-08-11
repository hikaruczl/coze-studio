package com.alibaba.cloud.ai.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bots")
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long spaceId;

    private String iconUri;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long ownerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "bot_connector_ids", joinColumns = @JoinColumn(name = "bot_id"))
    @Column(name = "connector_id")
    private List<Long> connectorIds;

    private String version;

    @Column(columnDefinition = "TEXT")
    private String versionDesc;

    private Long publishRecordId;

    @Enumerated(EnumType.ORDINAL)
    private PublishStatus publishStatus;

    private Long createdAt;

    private Long updatedAt;



    private Long publishedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getConnectorIds() {
        return connectorIds;
    }

    public void setConnectorIds(List<Long> connectorIds) {
        this.connectorIds = connectorIds;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }



    public Long getPublishRecordId() {
        return publishRecordId;
    }

    public void setPublishRecordId(Long publishRecordId) {
        this.publishRecordId = publishRecordId;
    }

    public PublishStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(PublishStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Long publishedAt) {
        this.publishedAt = publishedAt;
    }
}
