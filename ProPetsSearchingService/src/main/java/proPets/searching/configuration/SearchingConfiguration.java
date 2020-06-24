package proPets.searching.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.client.RestTemplate;

import proPets.searching.model.PostSearchData;

@Configuration
@ManagedResource
public class SearchingConfiguration {

	Map<String, PostSearchData> posts = new ConcurrentHashMap<>();
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Value("${radius.dog}")
	String radiusDog;
	
	public String getRadiusDog() {
		return radiusDog;
	}
	
	@Value("${radius.cat}")
	String radiusCat;
	
	public String getRadiusCat() {
		return radiusCat;
	}
	
	@Value("${radius.birds}")
	String radiusBirds;
	
	public String getRadiusBirds() {
		return radiusBirds;
	}
}