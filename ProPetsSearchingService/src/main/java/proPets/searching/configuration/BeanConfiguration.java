package proPets.searching.configuration;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Getter
public class BeanConfiguration {
	private double distanceDog;
	private double distanceCat;
	private double distanceBird;
	private double distanceGeneral;
	private String baseConvertUrl;
	
	public BeanConfiguration(double distanceDog, double distanceCat, double distanceBird, double distanceGeneral,
			String baseConvertUrl) {

		this.distanceDog = distanceDog;
		this.distanceCat = distanceCat;
		this.distanceBird = distanceBird;
		this.distanceGeneral = distanceGeneral;
		this.baseConvertUrl = baseConvertUrl;
	}


}
