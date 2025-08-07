package com.zai.weather.app.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.WeatherReportSourceManager;

@WebMvcTest(WeatherReportController.class)
class WeatherReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
	
    @MockitoBean
	private WeatherReportSourceManager weatherReportSourceManager;
    
    @ParameterizedTest
    @CsvSource({"1.1,2.2" , ","})
	void getWeatherReport_shouldReturnWeatherReport(String windSpeed, String temperatureDegrees) throws Exception {
		
		WeatherReport weatherReport = new WeatherReport(windSpeed, temperatureDegrees);
		when(weatherReportSourceManager.getWeatherReport(anyString())).thenReturn(weatherReport);
		
		 mockMvc.perform(get("http://localhost:8080/v1/weather?city=Melbourne"))
         .andExpect(status().isOk())
         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
         .andDo(print())
         .andExpect(jsonPath("$.windSpeed").value(windSpeed))
		 .andExpect(jsonPath("temperatureDegrees").value(temperatureDegrees));
         
         verify(weatherReportSourceManager).getWeatherReport(anyString());
		
	}
    
    @Test
	void getWeatherReport_shouldCallControllerAdvice() throws Exception {
    	
		when(weatherReportSourceManager.getWeatherReport(anyString())).thenThrow(HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "not found", null, null, null));

		 mockMvc.perform(get("http://localhost:8080/v1/weather?city=Melbourne"))
		 	.andExpect(status().isBadRequest());
		 
         verify(weatherReportSourceManager).getWeatherReport(anyString());
		
	}

}
