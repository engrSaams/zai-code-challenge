package com.zai.weather.app.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@Component
public class JsonUtil {

	public JsonNode convertToJsonNode(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = JsonNodeFactory.instance.objectNode();
		
		try {
			jsonNode = objectMapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jsonNode;
		
	}
	
}
