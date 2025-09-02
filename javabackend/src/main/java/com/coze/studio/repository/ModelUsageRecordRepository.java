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

package com.coze.studio.repository;

import com.coze.studio.entity.ModelUsageRecord;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 模型使用记录数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface ModelUsageRecordRepository {

    /**
     * 根据请求ID查找记录
     */
    Optional<ModelUsageRecord> findByRequestId(String requestId);

    /**
     * 根据会话ID查找记录
     */
    List<ModelUsageRecord> findBySessionIdOrderByCreatedAtAsc(String sessionId);

        /**
     * 统计记录总数
     */
    long countByUsageDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计成功记录数
     */
    long countByStatusAndUsageDateBetween(String status, LocalDateTime startDate, LocalDateTime endDate);

}
