package proPets.searching.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class ConvertedPostDto { 
	
	String id;
	String email; //author's email
	String flag;
	String type;
	String distFeatures;
	Set<String> picturesTags;
	Double[] location;

}
