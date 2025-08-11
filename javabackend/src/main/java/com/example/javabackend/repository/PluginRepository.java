package com.example.javabackend.repository;

import com.example.javabackend.model.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginRepository extends JpaRepository<Plugin, Long> {
}
