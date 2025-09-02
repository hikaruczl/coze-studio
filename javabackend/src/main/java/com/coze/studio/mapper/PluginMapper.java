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

package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.Plugin;
import org.apache.ibatis.annotations.Mapper;

/**
 * Plugin Mapper接口
 * 
 * @author coze-dev
 */
@Mapper
public interface PluginMapper extends BaseMapper<Plugin> {
    
    // BaseMapper已经提供了基本的CRUD方法：
    // - selectById(Serializable id)
    // - insert(T entity)
    // - updateById(T entity)
    // - deleteById(Serializable id)
    // - selectPage(IPage<T> page, Wrapper<T> queryWrapper)
    
}
