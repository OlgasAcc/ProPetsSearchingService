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
	
	@Value("${distance.dog}")
	double distanceDog;
	
	public double getDistanceDog() {
		return distanceDog;
	}
	
	@Value("${distance.cat}")
	double distanceCat;
	
	public double getDistanceCat() {
		return distanceCat;
	}
	
	@Value("${distance.bird}")
	double distanceBird;
	
	public double getDistanceBird() {
		return distanceBird;
	}
	
	@Value("${distance.general}")
	double distanceGeneral;
	
	public double getDistanceGeneral() {
		return distanceGeneral;
	}
}