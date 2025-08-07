package com.zai.weather.app.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;

@ExtendWith(MockitoExtension.class)
class JsonUtilTest {

	private String jsonString = "{\"name\":\"Isaac Newton\",\"age\":21,\"city\":\"New York\"}";
	
	@InjectMocks
	private JsonUtil jsonUtil;
	
	@Test
	void convertToJsonNode_shouldReturnValidJsonNode() {
		JsonNode jsonNode = jsonUtil.convertToJsonNode(jsonString);
		
		assertEquals("Isaac Newton", jsonNode.get("name").asText());
		assertEquals("21", jsonNode.get("age").asText());
		assertEquals("New York", jsonNode.get("city").asText());
	}

}
