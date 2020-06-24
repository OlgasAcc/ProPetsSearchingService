package proPets.searching.service;

import proPets.searching.dto.ConvertedPostDto;

public interface SearchingService {

	Iterable<String> findPostsByRadius(ConvertedPostDto convertedPostDto);

	void addPost(ConvertedPostDto convertedPostDto);
	
	

}
