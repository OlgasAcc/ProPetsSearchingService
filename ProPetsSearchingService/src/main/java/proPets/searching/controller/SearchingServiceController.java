package proPets.searching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import proPets.searching.dto.ConvertedPostDto;
import proPets.searching.service.SearchingService;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/search/v1")

public class SearchingServiceController {

	@Autowired
	SearchingService searchingService;

	@PostMapping("/post")
	public ResponseEntity<String> addPost(@RequestBody ConvertedPostDto convertedPostDto) throws Exception {	
		HttpHeaders newHeaders = new HttpHeaders();
		newHeaders.add("Content-Type", "application/json");
		searchingService.addPost(convertedPostDto);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/find/location")
	public Iterable<String> getPostsByRadius(@RequestBody ConvertedPostDto convertedPostDto) throws Exception {		
		return searchingService.findPostsByRadius(convertedPostDto);
	}
}
