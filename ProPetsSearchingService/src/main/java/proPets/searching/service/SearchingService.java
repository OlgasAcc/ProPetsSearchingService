package proPets.searching.service;

import proPets.searching.dto.RequestDto;

public interface SearchingService {

	Iterable<String> findPostsByDistance (String address, String flag);

	void addPost(RequestDto requestDto);
	
	

}
