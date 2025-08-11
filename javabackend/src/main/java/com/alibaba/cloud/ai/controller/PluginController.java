package com.alibaba.cloud.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plugin")
public class PluginController {

    private final ApplicationContext applicationContext;

    @Autowired
    public PluginController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/list")
    public List<Map<String, String>> listPlugins() {
        // Find all beans that are registered as Function tools
        String[] functionBeanNames = applicationContext.getBeanNamesForType(Function.class);

        return Arrays.stream(functionBeanNames)
                .map(name -> {
                    // Get the description from the @Description annotation on the bean definition
                    Description description = applicationContext.findAnnotationOnBean(name, Description.class);
                    return Map.of(
                            "name", name,
                            "description", description != null ? description.value() : "No description"
                    );
                })
                .collect(Collectors.toList());
    }
}
