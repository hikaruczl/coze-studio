package com.alibaba.cloud.ai.config;

import com.alibaba.cloud.ai.tool.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class ToolConfiguration {

    @Bean
    @Description("Get the weather in a specific location")
    public Function<WeatherService.Request, WeatherService.Response> weatherFunction() {
        return new WeatherService.WeatherFunction();
    }

}
