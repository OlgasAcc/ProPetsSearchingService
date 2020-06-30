package proPets.searching.dao;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import proPets.searching.model.PostSearchData;


public interface SearchingServiceRepository extends ElasticsearchRepository<PostSearchData, String> {

	@Query("{\"bool\":{\"must\":{\"match_all\":{}},\"filter\":{\"geo_distance\":{\"distance\":\"?2km\",\"location\":{\"lat\":?0,\"lon\":?1}}}}}")
	Set<PostSearchData> findIdByDistance(double latitude, double longitude, double distance);

	//test (only features)
	@Query("{\"query_string\":{\"fields\":[\"distFeatures\"],\"query\":\"?0\",\"minimum_should_match\":3}}")
	Iterable<PostSearchData> getStatsByFeatures(String distFeatures);
	
	//test (only type and features)
	@Query("{\"bool\":{\"filter\":[{\"type\":\"?0\"},{\"query_string\":{\"fields\":[\"distFeatures\"],\"query\":\"?1\",\"minimum_should_match\":3}}]}}")
	Iterable<PostSearchData> getStatsByTypeAndFeatures(String type, String distFeatures);
}


