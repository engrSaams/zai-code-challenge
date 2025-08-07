package com.zai.weather.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.WeatherReportSource;
import com.zai.weather.app.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenWeatherMapAPI implements WeatherReportSource {

	@Value("${open.weather.url}")
	private String openWeatherUrl;

	@Value("${open.weather.api.key:default_value}")
	private String openWeatherApiKey;

	private RestTemplate restTemplate;
	private JsonUtil jsonUtil;

	@Autowired
	public OpenWeatherMapAPI(RestTemplate restTemplate, JsonUtil jsonUtil) {
		this.restTemplate = restTemplate;
		this.jsonUtil = jsonUtil;
	}
	
	@Override
	public WeatherReport getWeatherReport(String city) {
		
		WeatherReport weatherReport = new WeatherReport();
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		try {
			response = restTemplate.getForEntity(buildUrl(city), String.class);
		} catch (RestClientException e) {
			log.error("Error in connecting to OpenWeatherMapAPI: " + response.getStatusCode());
			return weatherReport;
		}

		JsonNode responseJson = jsonUtil.convertToJsonNode(response.getBody());
		
		weatherReport.setWindSpeed(responseJson.get("wind").get("speed").toPrettyString());
		weatherReport.setTemperatureDegrees(responseJson.get("main").get("temp").toPrettyString());
		
		log.info("Successfully called OpenWeatherMap API: " + weatherReport.toString());
		return weatherReport;
		
	}
	
	@Override
	public String buildUrl(String city) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(openWeatherUrl)
				.queryParam("q", city + ",AU")
				.queryParam("appid", openWeatherApiKey);
		return builder.toUriString();
	}

}
