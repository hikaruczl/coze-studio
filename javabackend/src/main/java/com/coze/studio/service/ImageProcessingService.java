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

import java.util.Map;

/**
 * 图像处理服务接口
 * 
 * @author coze-dev
 */
public interface ImageProcessingService {

    /**
     * 生成图像
     */
    ImageGenerationResult generateImage(ImageGenerationRequest request);

    /**
     * 处理图像
     */
    ImageProcessingResult processImage(ImageProcessingRequest request);

    /**
     * 分析图像
     */
    ImageAnalysisResult analyzeImage(ImageAnalysisRequest request);

    /**
     * 图像生成请求
     */
    class ImageGenerationRequest {
        private String prompt;
        private String style;
        private String size;
        private int count;
        private String model;
        private Map<String, Object> parameters;

        // Getters and setters
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
        
        public String getStyle() { return style; }
        public void setStyle(String style) { this.style = style; }
        
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    /**
     * 图像处理请求
     */
    class ImageProcessingRequest {
        private String imageUrl;
        private String operation;
        private Map<String, Object> parameters;

        // Getters and setters
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    /**
     * 图像分析请求
     */
    class ImageAnalysisRequest {
        private String imageUrl;
        private String analysisType;
        private Map<String, Object> parameters;

        // Getters and setters
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getAnalysisType() { return analysisType; }
        public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    /**
     * 图像生成结果
     */
    class ImageGenerationResult {
        private boolean success;
        private String[] imageUrls;
        private String error;
        private long generationTime;
        private Map<String, Object> metadata;

        public ImageGenerationResult(boolean success, String[] imageUrls, String error, 
                                   long generationTime, Map<String, Object> metadata) {
            this.success = success;
            this.imageUrls = imageUrls;
            this.error = error;
            this.generationTime = generationTime;
            this.metadata = metadata;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String[] getImageUrls() { return imageUrls; }
        public String getError() { return error; }
        public long getGenerationTime() { return generationTime; }
        public Map<String, Object> getMetadata() { return metadata; }
    }

    /**
     * 图像处理结果
     */
    class ImageProcessingResult {
        private boolean success;
        private String processedImageUrl;
        private String error;
        private long processingTime;
        private Map<String, Object> metadata;

        public ImageProcessingResult(boolean success, String processedImageUrl, String error, 
                                   long processingTime, Map<String, Object> metadata) {
            this.success = success;
            this.processedImageUrl = processedImageUrl;
            this.error = error;
            this.processingTime = processingTime;
            this.metadata = metadata;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getProcessedImageUrl() { return processedImageUrl; }
        public String getError() { return error; }
        public long getProcessingTime() { return processingTime; }
        public Map<String, Object> getMetadata() { return metadata; }
    }

    /**
     * 图像分析结果
     */
    class ImageAnalysisResult {
        private boolean success;
        private Map<String, Object> analysisData;
        private String error;
        private long analysisTime;

        public ImageAnalysisResult(boolean success, Map<String, Object> analysisData, 
                                 String error, long analysisTime) {
            this.success = success;
            this.analysisData = analysisData;
            this.error = error;
            this.analysisTime = analysisTime;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public Map<String, Object> getAnalysisData() { return analysisData; }
        public String getError() { return error; }
        public long getAnalysisTime() { return analysisTime; }
    }
}
