/*
 * Copyright 2025 coze-dev Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coze.studio.entity;

// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插件评价实体
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: plugin_ratings
// 索引和约束信息已移至数据库DDL脚本
public class PluginRating extends BaseEntity {

    /**
     * 用户ID
     */
        private Long userId;

    /**
     * 插件ID
     */
        private Long pluginId;

    /**
     * 评分（1-5星）
     */
        private Integer rating;

    /**
     * 评价内容
     */
        private String comment;

    /**
     * 是否匿名
     */
        private Boolean isAnonymous = false;

    /**
     * 评价的插件版本
     */
        private String pluginVersion;

    /**
     * 是否有帮助的投票数
     */
        private Integer helpfulVotes = 0;

    /**
     * 总投票数
     */
        private Integer totalVotes = 0;

    /**
     * 评价状态：ACTIVE-有效, HIDDEN-隐藏, DELETED-已删除
     */
        private String status = "ACTIVE";

    /**
     * 管理员回复
     */
        private String adminReply;

    /**
     * 管理员回复时间
     */
        private java.time.LocalDateTime adminReplyAt;

    /**
     * 回复的管理员ID
     */
        private Long adminId;


    // Lombok生成的getter/setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public Integer getHelpfulVotes() {
        return helpfulVotes;
    }

    public void setHelpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
