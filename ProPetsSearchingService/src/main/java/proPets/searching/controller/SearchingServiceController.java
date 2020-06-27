package proPets.searching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proPets.searching.dto.RequestDto;
import proPets.searching.service.SearchingService;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/search/v1")

public class SearchingServiceController {

	@Autowired
	SearchingService searchingService;

	@PostMapping("/post")
	public HttpStatus addPost(@RequestBody RequestDto requestDto) throws Exception {
		HttpHeaders newHeaders = new HttpHeaders();
		newHeaders.add("Content-Type", "application/json");
		searchingService.addPost(requestDto);
		return HttpStatus.OK;
		// ResponseEntity.ok().build();
	}

	@GetMapping("/location")
	public Iterable<String> getPostsByDistance(@RequestParam ("address") String address, @RequestParam ("flag") String flag) throws Exception {
		return searchingService.findPostsByDistance(address,flag);
	}
	
}
