package proPets.searching.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import proPets.searching.model.PostSearchData;

@Configuration
@RefreshScope
public class SearchingConfiguration {

	Map<String, PostSearchData> posts = new ConcurrentHashMap<>();
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Value("${distance.dog}")
	double distanceDog;
	
	@RefreshScope
	public double getDistanceDog() {
		return distanceDog;
	}
	
	@Value("${distance.cat}")
	double distanceCat;
	
	@RefreshScope
	public double getDistanceCat() {
		return distanceCat;
	}
	
	@Value("${distance.bird}")
	double distanceBird;
	
	@RefreshScope
	public double getDistanceBird() {
		return distanceBird;
	}
	
	@Value("${distance.general}")
	double distanceGeneral;
	
	@RefreshScope
	public double getDistanceGeneral() {
		return distanceGeneral;
	}
	
	@Value("${base.convert.url}")
	String baseConvertUrl;
	
	@RefreshScope
	public String getBaseConvertUrl() {
		return baseConvertUrl;
	}
}