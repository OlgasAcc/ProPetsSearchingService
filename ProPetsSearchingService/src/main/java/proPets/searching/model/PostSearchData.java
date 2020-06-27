package proPets.searching.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

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
	//@Field(type = FieldType.Keyword)
	String distFeatures;
	//@Field(type = FieldType.Keyword)
	ArrayList<String> picturesTags;
	@GeoPointField
	GeoPoint location;
}
