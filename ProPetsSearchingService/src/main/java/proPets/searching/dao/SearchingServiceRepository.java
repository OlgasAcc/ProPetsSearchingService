package proPets.searching.dao;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import proPets.searching.model.PostSearchData;

public interface SearchingServiceRepository extends ElasticsearchRepository<PostSearchData, String> {

	@Query("{\"bool\":{\"must\":{\"match_all\":{}},\"filter\":{\"geo_distance\":{\"distance\":\"?2km\",\"location\":{\"lat\":?0,\"lon\":?1}}}}}")
	Set<PostSearchData> findIdByDistance(double latitude, double longitude, double distance);
	
	@Query("{\"query_string\":{\"fields\":[\"distFeatures\"],\"query\":\"?0\",\"minimum_should_match\":3}}")
	Set<PostSearchData> getStatsByFeatures(String distFeatures);

	@Query("{\"bool\":{\"must\":[{\"match\":{\"type\":{\"query\":\"?0\"}}},{\"multi_match\":{\"query\":\"?1\",\"fields\":[\"distFeatures\"],\"minimum_should_match\":3}},{\"multi_match\":{\"query\":\"?5\",\"fields\":[\"picturesTags\"],\"minimum_should_match\":4}}],\"filter\":{\"geo_distance\":{\"distance\":\"?4km\",\"location\":{\"lat\":?2,\"lon\":?3}}}}}")
	Set<PostSearchData> getIntersectedPosts(String type, String distFeatures, double lat, double lon,
			double distance, String picturesTags);

	
	
	
	
	
	
	// test (only type and features)
	@Query("{\"bool\":{\"must\":[{\"match\":{\"type\":{\"query\":\"?0\"}}},{\"multi_match\":{\"query\":\"?1\",\"fields\":[\"distFeatures\"],\"minimum_should_match\":3}}]}}")
	Iterable<PostSearchData> getStatsByTypeAndFeatures(String type, String distFeatures);
}
