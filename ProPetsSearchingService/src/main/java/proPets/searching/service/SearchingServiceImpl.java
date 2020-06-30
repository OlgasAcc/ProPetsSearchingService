package proPets.searching.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import proPets.searching.configuration.SearchingConfiguration;
import proPets.searching.dao.SearchingServiceRepository;
import proPets.searching.dto.ConvertedPostDto;
import proPets.searching.dto.RequestDto;
import proPets.searching.dto.RequestLocationDto;
import proPets.searching.exceptions.PostNotFoundException;
import proPets.searching.model.PostSearchData;

@Service
public class SearchingServiceImpl implements SearchingService {

	@Autowired
	SearchingConfiguration searchConfiguration;
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;

	@Override
	public String[] searchPostsByDistance(String address, String flag) {
		RequestLocationDto requestLocationDto = getRequestLocationDtoByAddress(address);
		
		Set<PostSearchData> listPosts = searchingServiceRepository.findIdByDistance(
				requestLocationDto.getLocation()[0], requestLocationDto.getLocation()[1],
				searchConfiguration.getDistanceGeneral());
		if (listPosts.isEmpty()) {
			return new String[0];
		} else {
			String flagToSearch = flag.equalsIgnoreCase("lost") ? "found" : "lost";
			List<String> list = listPosts.stream()
					.filter(p -> p.getFlag().equalsIgnoreCase(flagToSearch))
					.map(p -> p.getId())
					.collect(Collectors.toList());
			return list.toArray(new String[0]);
		}
		
	}
	

	@Override
	public void addPost(RequestDto requestDto) {
		System.out.println("im going to convert");
		ConvertedPostDto convertedPostDto = convertRequestDtoToConvertedPostDto(requestDto);
		
		GeoPoint location = new GeoPoint(convertedPostDto.getLocation()[0], convertedPostDto.getLocation()[1]);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < convertedPostDto.getPicturesTags().length; i++) {
			list.add(convertedPostDto.getPicturesTags()[i]);
		}
		PostSearchData postSearchData = PostSearchData.builder()
					.id(convertedPostDto.getId())
					.email(convertedPostDto.getEmail())
					.flag(convertedPostDto.getFlag())
					.type(convertedPostDto.getType())
					.distFeatures(convertedPostDto.getDistFeatures())
					.picturesTags(list)
					.location(location)
					.build();
		
		searchingServiceRepository.save(postSearchData);
		
		System.out.println("89: done!");
	}
	
	
	@Override
	public void editPost(RequestDto requestDto) throws PostNotFoundException {
		System.out.println("im going to convert");
		ConvertedPostDto convertedPostDto = convertRequestDtoToConvertedPostDto(requestDto);
		
		PostSearchData postSearchData = searchingServiceRepository.findById(convertedPostDto.getId())
				.orElseThrow(() -> new PostNotFoundException());
		GeoPoint location = new GeoPoint(convertedPostDto.getLocation()[0], convertedPostDto.getLocation()[1]);
		ArrayList<String> tags = new ArrayList<String>();
		for (int i = 0; i < convertedPostDto.getPicturesTags().length; i++) {
			tags.add(convertedPostDto.getPicturesTags()[i]);
		}	
			if(convertedPostDto.getDistFeatures()!=null) {postSearchData.setDistFeatures(convertedPostDto.getDistFeatures());}
			if(convertedPostDto.getLocation()!=null) {postSearchData.setLocation(location);}
			if(convertedPostDto.getPicturesTags()!=null) {postSearchData.setPicturesTags(tags);}
		
		searchingServiceRepository.save(postSearchData);
		System.out.println("122: done!");
	}
	
	
	@Override
	public String[] searchMatchingPosts(String postId, String flag) throws PostNotFoundException {
		PostSearchData postSearchData = searchingServiceRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException());
		// написать алгоритм поиска по 4 параметрам
		return null;
	}
	
	
	// UTILS!
	//___________________________________________________________
	
	
	private RequestLocationDto getRequestLocationDtoByAddress(String address) {
		RestTemplate restTemplate = searchConfiguration.restTemplate();
		//String url = "https://propets-.../convert/v1/location";
		String url = "http://localhost:8084/convert/v1/location"; //to Converter Service
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("address",address);
		RequestEntity<String> request = new RequestEntity<String>(HttpMethod.PUT, builder.build().toUri());
		ResponseEntity<RequestLocationDto> response = restTemplate.exchange(request, RequestLocationDto.class);
		return response.getBody();
	}
	
	private ConvertedPostDto convertRequestDtoToConvertedPostDto(RequestDto requestDto) {
		RestTemplate restTemplate = searchConfiguration.restTemplate();
		// String url = "https://propets-.../convert/v1/post";
		String url = "http://localhost:8084/convert/v1/post"; // to Converter service
		try {
			HttpHeaders newHeaders = new HttpHeaders();
			newHeaders.add("Content-Type", "application/json");
			BodyBuilder requestBodyBuilder = RequestEntity.method(HttpMethod.PUT, URI.create(url)).headers(newHeaders);
			RequestEntity<RequestDto> request = requestBodyBuilder.body(requestDto);
			ResponseEntity<ConvertedPostDto> newResponse = restTemplate.exchange(request, ConvertedPostDto.class);
			return newResponse.getBody();
		} catch (HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Converting post is failed");
		}
	}
	
	
	
	
	
	
	

	//test
	@Override
	public Iterable<PostSearchData> getAllFromDB() {
		Iterable<PostSearchData> res = searchingServiceRepository.findAll();
		return res;
	}

	//test
	@Override
	public PostSearchData getById(String id) {
		PostSearchData post = searchingServiceRepository.findById(id).orElse(null);
		return post;
	}
	
	//test
	@Override
	public void cleanES() {
		searchingServiceRepository.deleteAll();
	}
	
	//test
	@Override
	public PostSearchData addPost1(RequestDto requestDto) {
		System.out.println("im going to convert");
		ConvertedPostDto convertedPostDto = convertRequestDtoToConvertedPostDto(requestDto);
		
		GeoPoint location = new GeoPoint(convertedPostDto.getLocation()[0], convertedPostDto.getLocation()[1]);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < convertedPostDto.getPicturesTags().length; i++) {
			list.add(convertedPostDto.getPicturesTags()[i]);
		}
		PostSearchData postSearchData = PostSearchData.builder()
					.id(convertedPostDto.getId())
					.email(convertedPostDto.getEmail())
					.flag(convertedPostDto.getFlag())
					.type(convertedPostDto.getType())
					.distFeatures(convertedPostDto.getDistFeatures())
					.picturesTags(list)
					.location(location)
					.build();
		
		searchingServiceRepository.save(postSearchData);
		return postSearchData;
	}


	@Override
	public Iterable<PostSearchData> getIntersectionStatsByFeatures(String postId, String flag) {
		PostSearchData post = searchingServiceRepository.findById(postId).orElse(null);
		String distFeatures = post.getDistFeatures();
		//Iterable<PostSearchData>posts=searchingServiceRepository.findAll();
		
		return searchingServiceRepository.getStatsByFeatures(distFeatures);
	}


	@Override
	public Iterable<PostSearchData> getIntersectionStatsByTypeAndFeatures(String postId, String flag) {
		PostSearchData post = searchingServiceRepository.findById(postId).orElse(null);
		String distFeatures = post.getDistFeatures();
		String type=post.getType();
		return searchingServiceRepository.getStatsByTypeAndFeatures(type,distFeatures);
	}


}

