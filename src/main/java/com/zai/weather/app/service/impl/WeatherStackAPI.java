package com.zai.weather.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.WeatherSource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherStackAPI implements WeatherSource {

	@Value("${weather.stack.url}")
	private String weatherStackUrl;

	@Value("${weather.stack.api.key}")
	private String weatherStackApiKey;

	private RestTemplate restTemplate;

	@Autowired
	public WeatherStackAPI(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@CachePut(value = "weatherCache", unless = "#result == null || #result.windSpeed == null || #result.temperatureDegrees == null")
	public WeatherReport perform() {

		WeatherReport weatherReport = new WeatherReport();
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weatherStackUrl)
				.queryParam("access_key", weatherStackApiKey)
				.queryParam("query", "Melbourne");
		String url = builder.toUriString();

		ObjectMapper objectMapper = new ObjectMapper();
		try {

			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			weatherReport.setWindSpeed(jsonNode.get("current").get("wind_speed").toPrettyString());
			weatherReport.setTemperatureDegrees(jsonNode.get("current").get("temperature").toPrettyString());
			log.info("Successfully called WeatherStack API: " + new ObjectMapper().writeValueAsString(weatherReport));

		} catch (Exception e) {

			log.error("Error in calling Weather Stack API: " + e.getLocalizedMessage());

		}

		return weatherReport;
	}

}
