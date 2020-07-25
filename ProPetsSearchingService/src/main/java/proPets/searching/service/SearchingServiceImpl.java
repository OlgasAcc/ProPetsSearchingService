package proPets.searching.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import proPets.searching.configuration.SearchingConfiguration;
import proPets.searching.dao.SearchingServiceRepository;
import proPets.searching.dto.ConvertedPostDto;
import proPets.searching.dto.RequestDto;
import proPets.searching.dto.RequestLocationDto;
import proPets.searching.dto.UserRemoveDto;
import proPets.searching.exceptions.PostNotFoundException;
import proPets.searching.model.PostSearchData;
import proPets.searching.service.utils.SearchUtil;

@Service
public class SearchingServiceImpl implements SearchingService {

	@Autowired
	SearchingConfiguration searchConfiguration;
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;
	
	@Autowired
	SearchUtil searchUtil;
	
	@Autowired
	SearchingDataExchangeService dataExchangeService;
	
	@Override
	public void addOrEditPost(RequestDto requestDto) throws PostNotFoundException{
		System.out.println("im going to convert");
		ConvertedPostDto convertedPostDto = searchUtil.convertRequestDtoToConvertedPostDto(requestDto);
		
		GeoPoint location = new GeoPoint(convertedPostDto.getLocation()[0], convertedPostDto.getLocation()[1]);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < convertedPostDto.getPicturesTags().length; i++) {
			list.add(convertedPostDto.getPicturesTags()[i]);
		}
		PostSearchData postSearchData = PostSearchData.builder()
					.id(convertedPostDto.getId()) // если пост есть в бд - перезапишет по id
					.email(convertedPostDto.getEmail())
					.flag(convertedPostDto.getFlag())
					.type(convertedPostDto.getType())
					.distFeatures(convertedPostDto.getDistFeatures())
					.picturesTags(list)
					.location(location)
					//TODO: .IsAuthorSubscribed(true)
					.build();
		
		searchingServiceRepository.save(postSearchData);
		System.out.println("saved in DS");
	}
	
	@Override
	public void removePost(String postId) throws PostNotFoundException {
		PostSearchData postSearchData = searchingServiceRepository.findById(postId).orElseThrow(()-> new PostNotFoundException());
		searchingServiceRepository.delete(postSearchData);
	}
	
	//user's manual searching in the current db by flag
	@Override
	public String[] searchPostsByDistance(String address, String flag) {
		RequestLocationDto requestLocationDto = searchUtil.getRequestLocationDtoByAddress(address);
		
		Set<PostSearchData> listPosts = searchingServiceRepository.findIdByDistance(
				requestLocationDto.getLocation()[0], requestLocationDto.getLocation()[1],
				searchConfiguration.getDistanceGeneral(), flag);
		if (listPosts.isEmpty()) {
			return new String[0];
		} else {
			List<String> list = listPosts.stream()
					.map(p -> p.getId())
					.collect(Collectors.toList());
			return list.toArray(new String[0]);
		}		
	}
	
	//user's manual searching in the current db by flag
	@Override
	public String[] searchPostsByMatchingFeatures(String distFeatures, String flag) throws PostNotFoundException {	
		return searchingServiceRepository.getPostsByFeatures(distFeatures, flag).stream()
				.map(p -> p.getId())
				.collect(Collectors.toList())
				.toArray(new String[0]);				
	}
	
	@Override
	public String[] getPostsIdsOfMatchingPosts(String postId, String flag) throws PostNotFoundException { 
		Set<PostSearchData> set = searchUtil.searchMatchingPosts(postId);
		return set.stream()
				.map(p -> p.getId())
				.collect(Collectors.toList())
				.toArray(new String[0]);	
	}
	
	@Override
	public String[] getAuthorsOfMatchingPosts(String postId, String flag) throws PostNotFoundException {
		Set<PostSearchData> set = searchUtil.searchMatchingPosts(postId);
		return set.stream()
				.map(p -> p.getEmail())
				.collect(Collectors.toList())
				.toArray(new String[0]);
	}
	
	// for cleaning the tail of posts of removed author
	@Override
	public String removePostsByAuthor(UserRemoveDto userRemoveDto) {
		String authorId = userRemoveDto.getUserId();			
		searchingServiceRepository.findAll().stream()
					.filter(p->p.getEmail().equalsIgnoreCase(authorId))
					.forEach(post->searchingServiceRepository.delete(post));	
		return authorId;
	}
	
	@Override
	public void unsubscribeFromEmailNotification(String authorId) {
		// TODO найти все посты по автору, в стриме поменять им поле IsAuthorSubscribed на false
		
	}

	
	
	//Tests
	//_____________________________________________________
	

	//test
	public Iterable<PostSearchData> getAllFromDB() {
		Iterable<PostSearchData> res = searchingServiceRepository.findAll();
		return res;
	}

	//test
	public PostSearchData getById(String id) {
		PostSearchData post = searchingServiceRepository.findById(id).orElse(null);
		return post;
	}
	
	//test
	public void cleanES() {
		searchingServiceRepository.deleteAll();
	}
}

