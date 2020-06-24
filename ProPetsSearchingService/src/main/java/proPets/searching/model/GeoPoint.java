package proPets.searching.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class GeoPoint implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	double latitude;
	double longitude;
}
