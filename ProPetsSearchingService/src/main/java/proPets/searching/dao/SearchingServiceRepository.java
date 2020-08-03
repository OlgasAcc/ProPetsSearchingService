package proPets.searching.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import proPets.searching.model.PostSearchData;

public interface SearchingServiceRepository extends ElasticsearchRepository<PostSearchData, String> {

	List<PostSearchData> findAll();
	
	//@Query("{\"bool\":{\"must\":{\"term\":{\"email\":\"?0\"}}}}")
	List<PostSearchData> findByEmail(String email);
	
	@Query("{\"bool\":{\"must\":{\"term\":{\"flag\":\"?3\"}},\"filter\":{\"geo_distance\":{\"distance\":\"?2km\",\"location\":{\"lat\":?0,\"lon\":?1}}}}}")
	Set<PostSearchData> findIdByDistance(double latitude, double longitude, double distance, String flag);
	
	@Query("{\"bool\":{\"must\":[{\"match\":{\"flag\":{\"query\":\"?1\"}}},{\"multi_match\":{\"query\":\"?0\",\"fields\":[\"distFeatures\"],\"minimum_should_match\":3}}]}}")
	Set<PostSearchData> getPostsByFeatures(String distFeatures, String flag);

	//@Query("{\"bool\":{\"must\":[{\"match\":{\"type\":{\"query\":\"?0\"}}},{\"multi_match\":{\"query\":\"?1\",\"fields\":[\"distFeatures\"],\"minimum_should_match\":3}},{\"multi_match\":{\"query\":\"?5\",\"fields\":[\"picturesTags\"],\"minimum_should_match\":4}}],\"filter\":{\"geo_distance\":{\"distance\":\"?4km\",\"location\":{\"lat\":?2,\"lon\":?3}}}}}")
	@Query("{\"bool\":{\"must\":[{\"multi_match\":{\"query\":\"?6\",\"fields\":[\"flag\"]}},{\"multi_match\":{\"query\":\"?0\",\"fields\":[\"type\"]}},{\"multi_match\":{\"query\":\"?1\",\"fields\":[\"distFeatures\"],\"minimum_should_match\":3}},{\"multi_match\":{\"query\":\"?5\",\"fields\":[\"picturesTags\"],\"minimum_should_match\":4}}],\"filter\":{\"geo_distance\":{\"distance\":\"?4km\",\"location\":{\"lat\":?2,\"lon\":?3}}}}}")
	Set<PostSearchData> getIntersectedPosts(String type, String distFeatures, double lat, double lon,
			double distance, String picturesTags, String flag);
}
