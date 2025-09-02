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

package com.coze.studio.constant;

/**
 * 通用常量类
 * 
 * @author coze-dev
 */
public final class CommonConstants {

    private CommonConstants() {
        // 防止实例化
    }

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * 默认页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 最大页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 默认排序字段
     */
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    /**
     * 默认排序方向
     */
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    /**
     * 日期时间格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 时区
     */
    public static final String TIME_ZONE = "GMT+8";

    /**
     * UTF-8 编码
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 默认语言
     */
    public static final String DEFAULT_LANGUAGE = "zh-CN";

    /**
     * 系统用户ID（用于系统操作）
     */
    public static final Long SYSTEM_USER_ID = 0L;

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "/images/default-avatar.png";

    /**
     * 默认Bot图标
     */
    public static final String DEFAULT_BOT_ICON = "/images/default-bot-icon.png";
}
