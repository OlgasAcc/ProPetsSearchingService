package proPets.searching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proPets.searching.dao.SearchingServiceRepository;
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
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;

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
	public ResponseEntity<LocationResponseDto> getMatchingPostsByDistance(@RequestParam("address") String address,
			@RequestParam("flag") String flag) throws Exception {
		String flagToSearch = flag.equalsIgnoreCase("lost") ? "found" : "lost";
		String[] arr = searchingService.searchPostsByDistance(address, flagToSearch); // для ручного поиска по локации: на странице клиент ищет посты в той же базе
		LocationResponseDto body = new LocationResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
	
	@GetMapping("/stats/features")
	public String[] getMatchingPostsIdsByFeatures(@RequestParam("postId") String postId,
			@RequestParam("flag") String flag) throws Exception {
		return searchingService.searchPostsByMatchingFeatures(postId, flag);
	}
		
	//его запустит ЛФ, когда юзер нажмет на ссылку в письме, чтобы отрисовать совпавшие посты на фронте
	@GetMapping("/all_matches")
	public ResponseEntity<LocationResponseDto> getMatchingPosts(@RequestParam("post") String postId,
			@RequestParam("flag") String flag) throws Exception {
		String[] arr = searchingService.getPostsIdsOfMatchingPosts(postId, flag);
		LocationResponseDto body = new LocationResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
	
	//ЛФ вызывает асинхронно. Ищет список авторов. Шлет 1 запрос со списком авторов в боди в мэйлинг. Второй в мэйлинг - автору нового поста
	// для теста - вызвать отсюда и проверить с email service
	@GetMapping("/notification")
	public ResponseEntity<LocationResponseDto> getMatchingPostsAuthors(@RequestParam("post") String postId,
			@RequestParam("flag") String flag) throws Exception {
		String[] authorsEmails = searchingService.getAuthorsOfMatchingPosts(postId, flag);
		LocationResponseDto body = new LocationResponseDto(authorsEmails);
		return ResponseEntity.ok().body(body);
	}
	
	// called by LF for removing all the "tail" by removed author in ES db
	@DeleteMapping("/cleaner")
	public ResponseEntity<String> removePostsByAuthor(@RequestParam ("authorId") String authorId) throws Exception {		
		searchingService.removePostsByAuthor(authorId);
		return ResponseEntity.ok().build();
	}
	
	
	
	
//____________________________________________________________
	
	
	//test
	@GetMapping("/stats")
	public Iterable<PostSearchData> getIntersectionStats(@RequestParam("postId") String postId,
			@RequestParam("flag") String flag) throws Exception {
		return searchingService.getIntersectionStats(postId, flag);
	}
	
	//test
	@GetMapping("/stats/type_features")
	public Iterable<PostSearchData> getIntersectionStatsByTypeAndFeatures(@RequestParam("postId") String postId,
			@RequestParam("flag") String flag) throws Exception {
		return searchingService.getIntersectionStatsByTypeAndFeatures(postId, flag);
	}
	
	//test
	@GetMapping("/find")
	public String[] getPostsByDist(@RequestParam("address") String address,
			@RequestParam("flag") String flag) throws Exception {
		return searchingService.searchPostsByDistance(address, flag);
	}
	
	//test
	@PostMapping("/add")
	public PostSearchData addPost1(@RequestBody RequestDto requestDto) throws Exception {
		return searchingService.addPost1(requestDto);
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
