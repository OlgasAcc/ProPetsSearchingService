package proPets.searching.dao;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import proPets.searching.model.PostSearchData;


public interface SearchingServiceRepository extends ElasticsearchRepository<PostSearchData, String> {

	@Query("{\"bool\":{\"must\":{\"match_all\":{}},\"filter\":{\"geo_distance\":{\"distance\":\"?2km\",\"location\":{\"lat\":?0,\"lon\":?1}}}}}")
	Set<PostSearchData> findIdByDistance(double latitude, double longitude, double distance);
}
