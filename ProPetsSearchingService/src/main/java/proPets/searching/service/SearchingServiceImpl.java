package proPets.searching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proPets.searching.configuration.SearchingConfiguration;
import proPets.searching.dao.SearchingServiceRepository;
import proPets.searching.dto.ConvertedPostDto;
import proPets.searching.model.PostSearchData;

@Service
public class SearchingServiceImpl implements SearchingService {

	@Autowired
	SearchingConfiguration searchConfiguration;
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;

	@Override
	public Iterable<String> findPostsByRadius(ConvertedPostDto convertedPostDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPost(ConvertedPostDto convertedPostDto) {
		PostSearchData postSearchData = searchingServiceRepository.findById(convertedPostDto.getId()).orElse(null);
		if(postSearchData!=null) {
			if(convertedPostDto.getDistFeatures()!=null) {postSearchData.setDistFeatures(convertedPostDto.getDistFeatures());}
			if(convertedPostDto.getLocation()!=null) {postSearchData.setLocation(convertedPostDto.getLocation());}
			if(convertedPostDto.getPicturesTags()!=null) {postSearchData.setPicturesTags(convertedPostDto.getPicturesTags());}
		} else {
			postSearchData = PostSearchData.builder()
					.id(convertedPostDto.getId())
					.email(convertedPostDto.getEmail())
					.flag(convertedPostDto.getFlag())
					.type(convertedPostDto.getType())
					.distFeatures(convertedPostDto.getDistFeatures())
					.location(convertedPostDto.getLocation())
					.build();
		}
		searchingServiceRepository.save(postSearchData);
	}
	
	

}
