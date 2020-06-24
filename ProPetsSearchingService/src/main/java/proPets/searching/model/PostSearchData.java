package proPets.searching.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of={"id"})
@Document(indexName="posts")

public class PostSearchData { 
	
	@Id
	String id;
	String email; //authorId
	String flag;
	String type;
	String distFeatures;
	Set<String> picturesTags;
	@GeoPointField
	Double[] location;
}
