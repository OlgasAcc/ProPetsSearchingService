package proPets.searching.service;

import proPets.searching.dto.RequestDto;
import proPets.searching.dto.UserRemoveDto;
import proPets.searching.exceptions.PostNotFoundException;

public interface SearchingService {

	void addOrEditPost(RequestDto requestDto) throws PostNotFoundException;
	
	void removePost(String postId) throws PostNotFoundException;
	
	String[] searchPostsByDistance(String address, String flag);
	
	String[] searchPostsByMatchingFeatures(String postId, String flag) throws PostNotFoundException;
	
	String[] getAuthorsOfMatchingPosts(String postId, String flag) throws PostNotFoundException;

	String[] getPostsIdsOfMatchingPosts(String postId, String flag) throws PostNotFoundException;

	String removePostsByAuthor(UserRemoveDto userRemoveDto);
	
	void unsubscribeFromEmailNotification (String authorId);
	
	



}
