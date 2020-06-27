package proPets.searching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proPets.searching.dto.LocationResponseDto;
import proPets.searching.dto.RequestDto;
import proPets.searching.model.PostSearchData;
import proPets.searching.service.SearchingService;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/search/v1")

public class SearchingServiceController {

	@Autowired
	SearchingService searchingService;

	@PutMapping("/post")
	public ResponseEntity<String> addPost(@RequestBody RequestDto requestDto) throws Exception {
		HttpHeaders newHeaders = new HttpHeaders();
		newHeaders.add("Content-Type", "application/json");
		searchingService.addPost(requestDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/location")
	public ResponseEntity<LocationResponseDto> getPostsByDistance(@RequestParam("address") String address,
			@RequestParam("flag") String flag) throws Exception {
		String[] arr = searchingService.findPostsByDistance(address, flag);
		LocationResponseDto body = new LocationResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
	
	//test
	@GetMapping("/getAll")
	public Iterable<PostSearchData>getAllFromDB(){
		return searchingService.getAllFromDB();
	}
	
	//test
	@GetMapping("/get/{id}")
	public PostSearchData getById(@PathVariable String id){
		return searchingService.getById(id);
	}
	
	//test
	@DeleteMapping("/clean")
	public void cleanES(){
		searchingService.cleanES();
	}
}
