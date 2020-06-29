package proPets.searching.service;

import proPets.searching.dto.RequestDto;
import proPets.searching.exceptions.PostNotFoundException;
import proPets.searching.model.PostSearchData;

public interface SearchingService {

	String[] searchPostsByDistance(String address, String flag);

	void addPost(RequestDto requestDto);
	
	void editPost(RequestDto requestDto) throws PostNotFoundException;

	String[] searchMatchingPosts(String postId, String flag) throws PostNotFoundException; 
	
	
	
	
	
// test
	Iterable<PostSearchData> getAllFromDB();

//test
	PostSearchData getById(String id);

//test
	void cleanES();
	
//test
	PostSearchData addPost1(RequestDto requestDto);




}
