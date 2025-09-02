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

package com.coze.studio.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * JSON 工具类
 *
 * @author coze-dev
 */
@Slf4j
public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
        // 防止实例化
    }

    /**
     * 对象转JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON转对象失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON转Map失败", e);
            return null;
        }
    }

    /**
     * 对象转Map
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Map转对象
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(map, clazz);
    }
/**
     * JSON字符串转列表（简单重载，按元素类型解析）
     * 最小桩实现：仅返回通用Object列表或空
     */
    public static <T> java.util.List<T> fromJsonToList(String json, Class<T> elementType) {
        if (json == null || json.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(java.util.List.class, elementType));
        } catch (Exception e) {
            log.error("JSON转List失败", e);
            return java.util.Collections.emptyList();
        }
    }

    // 兼容：fromJsonToMap(String, Class) 重载占位
    public static <T> java.util.Map<String, T> fromJsonToMap(String json, Class<T> valueType) {
        if (json == null || json.trim().isEmpty()) return java.util.Collections.emptyMap();
        try {
            return OBJECT_MAPPER.readValue(json,
                    OBJECT_MAPPER.getTypeFactory().constructMapType(java.util.Map.class, String.class, valueType));
        } catch (Exception e) {
            log.error("JSON转Map失败", e);
            return java.util.Collections.emptyMap();
        }
    }

    // 兼容：toPrettyJson(List<Map<String,Object>>) 占位
    public static String toPrettyJson(java.util.List<java.util.Map<String, Object>> list) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } catch (Exception e) {
            log.error("列表转Pretty JSON失败", e);
            return "[]";
        }
    }
}
