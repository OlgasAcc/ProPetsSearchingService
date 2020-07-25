package proPets.searching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proPets.searching.configuration.BeanConfiguration;
import proPets.searching.configuration.SearchingConfiguration;
import proPets.searching.dao.SearchingServiceRepository;
import proPets.searching.dto.RequestDto;
import proPets.searching.dto.ResponseDto;
import proPets.searching.dto.UserRemoveDto;
import proPets.searching.model.PostSearchData;
import proPets.searching.service.SearchingService;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/search/v1")

public class SearchingServiceController {

	@Autowired
	SearchingService searchingService;
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;

	@Autowired
	SearchingConfiguration searchingConfiguration;

	@RefreshScope
	@GetMapping("/config")
	public  BeanConfiguration getRefreshedData() {
		return new BeanConfiguration(searchingConfiguration.getDistanceBird(), searchingConfiguration.getDistanceCat(),searchingConfiguration.getDistanceDog(),searchingConfiguration.getDistanceGeneral(),searchingConfiguration.getBaseConvertUrl());
	}
	
	@PostMapping("/post")
	public ResponseEntity<String> addOrEditPost(@RequestBody RequestDto requestDto) throws Exception {
		searchingService.addOrEditPost(requestDto);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/post")
	public ResponseEntity<String> removePost(@RequestParam ("postId") String postId) throws Exception {
		searchingService.removePost(postId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/location")
	public ResponseEntity<ResponseDto> getMatchingPostsByDistance(@RequestParam("address") String address,
			@RequestParam("flag") String flag) throws Exception {
		String[] arr = searchingService.searchPostsByDistance(address, flag); // для ручного поиска по локации: на странице клиент ищет посты в той же базе
		ResponseDto body = new ResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
	
	@GetMapping("/features")
	public ResponseEntity<ResponseDto> getMatchingPostsIdsByFeatures(@RequestParam("features") String features,
			@RequestParam("flag") String flag) throws Exception {
		String[] arr = searchingService.searchPostsByMatchingFeatures(features, flag);
		ResponseDto body = new ResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
		
	//его запустит ЛФ, когда юзер нажмет на ссылку в письме, чтобы отрисовать совпавшие посты на фронте
	@GetMapping("/all_matched")
	public ResponseEntity<ResponseDto> getMatchingPosts(@RequestParam("postId") String postId,
			@RequestParam("flag") String flag) throws Exception {
		String[] arr = searchingService.getPostsIdsOfMatchingPosts(postId, flag);
		ResponseDto body = new ResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
	
	// for test purpose: to call from email service instead for using Kafka
	@GetMapping("/notification")
	public ResponseEntity<ResponseDto> getMatchingPostsAuthors(@RequestParam("postId") String postId,
			@RequestParam("flag") String flag) throws Exception {
		String[] authorsEmails = searchingService.getAuthorsOfMatchingPosts(postId, flag);
		ResponseDto body = new ResponseDto(authorsEmails);
		return ResponseEntity.ok().body(body);
	}
	
	// called by Accounting Service for removing all the "tail" by removed author in ES db
	@DeleteMapping("/cleaner")
	public ResponseEntity<String> cleanPostsByAuthor(@RequestBody UserRemoveDto userRemoveDto) throws Exception {		
		return ResponseEntity.ok(searchingService.removePostsByAuthor(userRemoveDto));
	}
	
	@PutMapping("/unsubscribe/{authorId}")
	public ResponseEntity<String> unsubcribeFromNotifications(@PathVariable String authorId) throws Exception {		
		searchingService.unsubscribeFromEmailNotification(authorId);
		return ResponseEntity.ok(authorId);
	}		
	
	
//____________________________________________________________
	
//	//test
	@GetMapping("/getAll")
	public Iterable<PostSearchData>getAllFromDB(){
		return searchingServiceRepository.findAll();
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
