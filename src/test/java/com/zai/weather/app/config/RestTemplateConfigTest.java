package com.zai.weather.app.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {

	@Test
	void restTemplate_shouldCreateBean() {
		RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
		RestTemplate restTemplate = restTemplateConfig.restTemplate();
		assertNotNull(restTemplate);
	}

}
