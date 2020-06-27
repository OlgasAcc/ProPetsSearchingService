package proPets.searching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ConvertedPostDto { 
	
	String id;
	String email; //author's email
	String flag;
	String type;
	String distFeatures;
	String[] picturesTags;
	Double[] location;

}
