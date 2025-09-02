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

package com.coze.studio.dto.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页响应格式
 *
 * @param <T> 数据类型
 * @author coze-dev
 */
@Data
public class PageResponse<T> {

    /**
     * 数据列表
     */
    private List<T> items;

    /**
     * 当前页码（从1开始）
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总记录数
     */
    private long totalElements;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否有上一页
     */
    private boolean hasPrevious;

    /**
     * 是否是第一页
     */
    private boolean isFirst;

    /**
     * 是否是最后一页
     */
    private boolean isLast;

    public PageResponse() {
    }

    public PageResponse(List<T> items, int page, int size, int totalPages, long totalElements) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = page < totalPages;
        this.hasPrevious = page > 1;
        this.isFirst = page == 1;
        this.isLast = page == totalPages;
    }

    /**
     * 从 Spring Data Page 对象创建分页响应
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1, // Spring Data 页码从0开始，转换为从1开始
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    /**
     * 便捷重载：从 Page<T> 创建响应并保持泛型
     */
    public static <T> PageResponse<T> of(List<T> items, Page<?> pageMeta) {
        PageResponse<T> resp = new PageResponse<>(items,
                pageMeta.getNumber() + 1,
                pageMeta.getSize(),
                pageMeta.getTotalPages(),
                pageMeta.getTotalElements());
        resp.setHasNext(pageMeta.hasNext());
        resp.setHasPrevious(pageMeta.hasPrevious());
        resp.setFirst(pageMeta.isFirst());
        resp.setLast(pageMeta.isLast());
        return resp;
    }

    /**
     * 将 List<T> 包装为 PageResponse，忽略分页元信息（占位用途）
     */
    public static <T> PageResponse<T> of(List<T> items) {
        return new PageResponse<>(items, 1, items != null ? items.size() : 0, 1, items != null ? items.size() : 0);
    }

    /**
     * 创建空的分页响应
     */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(List.of(), 1, 0, 0, 0);
    }


    // Lombok生成的getter/setter方法
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }
}
