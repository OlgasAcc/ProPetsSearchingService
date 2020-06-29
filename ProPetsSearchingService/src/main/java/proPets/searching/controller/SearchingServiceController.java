package proPets.searching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping("/post")
	public ResponseEntity<String> addPost(@RequestBody RequestDto requestDto) throws Exception {
		HttpHeaders newHeaders = new HttpHeaders();
		newHeaders.add("Content-Type", "application/json");
		searchingService.addPost(requestDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/location")
	public ResponseEntity<LocationResponseDto> getPostsByDistance(@RequestParam("address") String address,
			@RequestParam("flag") String flag) throws Exception {
		String flagToSearch = flag.equalsIgnoreCase("lost") ? "found" : "lost";
		String[] arr = searchingService.searchPostsByDistance(address, flagToSearch); // для ручного поиска по локации: на странице клиент ищет посты в той же базе
		LocationResponseDto body = new LocationResponseDto(arr);
		return ResponseEntity.ok().body(body);
	}
	
	@GetMapping("/all_matches")
	public ResponseEntity<LocationResponseDto> getMatchingPosts(@RequestParam("post") String postId,
			@RequestParam("flag") String flag) throws Exception {
		String[] arr = searchingService.searchMatchingPosts(postId, flag); // для ручного поиска по локации: на странице клиент ищет посты в той же базе
		LocationResponseDto body = new LocationResponseDto(arr);
		//сделать линк-запрос, куда положить в боди список айди-постов, в хедер - одноразовый код. Код сохранить в бд (монго?)
		// асинхронный запрос с линком в мейлинг-сервис
		// когда юзер перейдет по линку - ЛФ по этому запросу проверяет код: делает запрос сюда. Если код есть - то удаляет из бд и возвращает ок.
		// + ищет по этим айди посты и отрисовывает ленту
		return ResponseEntity.ok().body(body);
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
