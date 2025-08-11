package com.alibaba.cloud.ai.repository;

import com.alibaba.cloud.ai.model.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginRepository extends JpaRepository<Plugin, Long> {
}
