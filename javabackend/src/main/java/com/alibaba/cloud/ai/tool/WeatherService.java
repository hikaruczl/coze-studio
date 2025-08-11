package com.alibaba.cloud.ai.tool;

import java.util.function.Function;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    // The request structure for the weather tool
    public record Request(
            @JsonProperty(required = true, value = "location") @JsonPropertyDescription("The city and state, e.g. San Francisco, CA") String location,
            @JsonProperty(required = false, value = "unit") @JsonPropertyDescription("The unit of temperature, e.g. 'celsius' or 'fahrenheit'. Defaults to 'celsius'.") Unit unit) {
    }

    public enum Unit {
        CELSIUS("celsius"),
        FAHRENHEIT("fahrenheit");

        public final String value;

        Unit(String value) {
            this.value = value;
        }
    }

    // The response structure for the weather tool
    public record Response(double temp, Unit unit) {
    }

    public static class WeatherFunction implements Function<Request, Response> {

        @Override
        public Response apply(Request request) {
            // For this example, we'll return a mock response.
            // A real implementation would call an actual weather API.
            double temperature = 30.0;
            if (request.location().toLowerCase().contains("beijing")) {
                temperature = 25.0;
            } else if (request.location().toLowerCase().contains("london")) {
                temperature = 15.0;
            }

            Unit unit = request.unit() != null ? request.unit() : Unit.CELSIUS;

            if (unit == Unit.FAHRENHEIT) {
                temperature = (temperature * 9 / 5) + 32;
            }

            return new Response(temperature, unit);
        }
    }
}
