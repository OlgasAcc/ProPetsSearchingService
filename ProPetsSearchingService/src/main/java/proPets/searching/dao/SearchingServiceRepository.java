package proPets.searching.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import proPets.searching.model.PostSearchData;


public interface SearchingServiceRepository extends ElasticsearchRepository<PostSearchData, String>{


}
