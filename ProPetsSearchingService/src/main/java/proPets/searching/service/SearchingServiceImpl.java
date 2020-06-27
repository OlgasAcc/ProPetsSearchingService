package proPets.searching.service;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import proPets.searching.model.PostSearchData;

@Service
public class SearchingServiceImpl implements SearchingService {

	@Autowired
	SearchingConfiguration searchConfiguration;
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;

	@Override
	public Iterable<String> findPostsByDistance(String address, String flag) {
		RequestLocationDto requestLocationDto = getRequestLocationDtoByAddress(address); 
		System.out.println("39: " + requestLocationDto);
		Set<PostSearchData> listPosts = searchingServiceRepository.findAllByDistance(
				requestLocationDto.getLatitude(), requestLocationDto.getLongitude(),
				searchConfiguration.getDistanceGeneral());
		if (listPosts.isEmpty()) {
			return Collections.emptyList();
		} else {
		String flagToSearch=flag.equalsIgnoreCase("lost")?"found":"lost";
		return listPosts.stream()
				.filter(p->p.getFlag().equalsIgnoreCase(flagToSearch))
				.map(p->p.getId())
				.collect(Collectors.toList());
		}
		
	}
	
	private RequestLocationDto getRequestLocationDtoByAddress(String address) {
		RestTemplate restTemplate = searchConfiguration.restTemplate();
		//String url = "https://propets-.../convert/v1/location";
		String url = "http://localhost:8084/convert/v1/location"; //to Converter Service
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("address",address);
		RequestEntity<String> request = new RequestEntity<String>(HttpMethod.GET, builder.build().toUri());
		ResponseEntity<RequestLocationDto> response = restTemplate.exchange(request, RequestLocationDto.class);
		return response.getBody();
	}

	@Override
	public void addPost(RequestDto requestDto) {
		System.out.println("im going to convert");
		ConvertedPostDto convertedPostDto = convertRequestDtoToConvertedPostDto(requestDto);
		System.out.println(convertedPostDto.getId());
		
		//this is for editing
		//PostSearchData postSearchData = searchingServiceRepository.findById(convertedPostDto.getId()).orElse(null);
		//if(postSearchData!=null) {
		//	if(convertedPostDto.getDistFeatures()!=null) {postSearchData.setDistFeatures(convertedPostDto.getDistFeatures());}
		//	if(convertedPostDto.getLocation()!=null) {postSearchData.setLocation(convertedPostDto.getLocation());}
		//	if(convertedPostDto.getPicturesTags()!=null) {postSearchData.setPicturesTags(convertedPostDto.getPicturesTags());}
		//} else {
		PostSearchData postSearchData = PostSearchData.builder()
					.id(convertedPostDto.getId())
					.email(convertedPostDto.getEmail())
					.flag(convertedPostDto.getFlag())
					.type(convertedPostDto.getType())
					.distFeatures(convertedPostDto.getDistFeatures())
					.location(convertedPostDto.getLocation())
					.build();
		
		searchingServiceRepository.save(postSearchData);
		System.out.println("89: done!");
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

}

