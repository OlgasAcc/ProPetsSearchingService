package proPets.searching.service.utils;

import java.net.URI;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

@Component
public class SearchUtil {

	@Autowired
	SearchingServiceRepository searchingServiceRepository;
	
	@Autowired
	SearchingConfiguration searchConfiguration;
	
	public Set<PostSearchData> searchMatchingPosts(String postId) throws PostNotFoundException {
		PostSearchData post = searchingServiceRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException());
		String type = post.getType();
		String flagToSearch = post.getFlag().equalsIgnoreCase("lost") ? "found" : "lost";
		double distance = searchConfiguration.getDistanceGeneral();
		if (type.equalsIgnoreCase("cat")) {
			distance = searchConfiguration.getDistanceCat();
		}
		if (type.equalsIgnoreCase("dog")) {
			distance = searchConfiguration.getDistanceDog();
		}
		if (type.equalsIgnoreCase("parrot")) {
			distance = searchConfiguration.getDistanceBird();
		}
		return searchingServiceRepository.getIntersectedPosts(type, post.getDistFeatures(), post.getLocation().getLat(),
				post.getLocation().getLon(), distance, post.getPicturesTags().toString(), flagToSearch);
	}

	public RequestLocationDto getRequestLocationDtoByAddress(String address) {
		RestTemplate restTemplate = searchConfiguration.restTemplate();
		String url = searchConfiguration.getBaseConvertUrl() + "convert/v1/location";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("address", address);
		RequestEntity<String> request = new RequestEntity<String>(HttpMethod.PUT, builder.build().toUri());
		ResponseEntity<RequestLocationDto> response = restTemplate.exchange(request, RequestLocationDto.class);
		return response.getBody();
	}

	public ConvertedPostDto convertRequestDtoToConvertedPostDto(RequestDto requestDto) {
		RestTemplate restTemplate = searchConfiguration.restTemplate();
		String url = searchConfiguration.getBaseConvertUrl() + "convert/v1/post";
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
