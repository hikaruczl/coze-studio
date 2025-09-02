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

package com.coze.studio.service;

import java.util.List;
import java.util.Map;

/**
 * 数据集服务接口
 * 
 * @author coze-dev
 */
public interface DatasetService {

    /**
     * 查询数据集
     */
    DatasetQueryResult queryDataset(String datasetId, Map<String, Object> queryParams);

    /**
     * 写入数据集
     */
    DatasetWriteResult writeDataset(String datasetId, List<Map<String, Object>> data);

    /**
     * 更新数据集
     */
    DatasetWriteResult updateDataset(String datasetId, Map<String, Object> updateData, Map<String, Object> condition);

    /**
     * 删除数据集数据
     */
    DatasetWriteResult deleteDataset(String datasetId, Map<String, Object> condition);

    /**
     * 数据集查询结果
     */
    class DatasetQueryResult {
        private boolean success;
        private List<Map<String, Object>> data;
        private long totalCount;
        private String error;
        private long queryTime;

        public DatasetQueryResult(boolean success, List<Map<String, Object>> data, long totalCount, 
                                String error, long queryTime) {
            this.success = success;
            this.data = data;
            this.totalCount = totalCount;
            this.error = error;
            this.queryTime = queryTime;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public List<Map<String, Object>> getData() { return data; }
        public long getTotalCount() { return totalCount; }
        public String getError() { return error; }
        public long getQueryTime() { return queryTime; }
    }

    /**
     * 数据集写入结果
     */
    class DatasetWriteResult {
        private boolean success;
        private int affectedRows;
        private String error;
        private long writeTime;

        public DatasetWriteResult(boolean success, int affectedRows, String error, long writeTime) {
            this.success = success;
            this.affectedRows = affectedRows;
            this.error = error;
            this.writeTime = writeTime;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public int getAffectedRows() { return affectedRows; }
        public String getError() { return error; }
        public long getWriteTime() { return writeTime; }
    }
}
