package proPets.searching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import proPets.searching.dao.SearchingServiceRepository;
import proPets.searching.dto.EmailMatchedPostsAuthorsDto;
import proPets.searching.dto.EmailNewPostAuthorDto;
import proPets.searching.dto.PostMQDto;
import proPets.searching.exceptions.EmptyDataDtoException;
import proPets.searching.exceptions.PostNotFoundException;
import proPets.searching.model.PostSearchData;

@Service
//@Slf4j
public class SearchingDataExchangeService {
	
	@Autowired
	SearchingServiceRepository searchingServiceRepository;
	
	@Autowired
	DataExchange dataExchange;
	
	@Autowired
	SearchingService searchingService;

	@StreamListener(DataExchange.INPUT)
	public void handlePostData(@Payload PostMQDto postMQDto) throws JsonMappingException, JsonProcessingException, PostNotFoundException, EmptyDataDtoException {
		if (postMQDto != null) {
			PostSearchData postSearchData = searchingServiceRepository.findById(postMQDto.getPostId()).orElse(null);
			if (postSearchData != null) {
				String authorId = postSearchData.getEmail();
				String postId = postSearchData.getId();
				String flag = postSearchData.getFlag();

				EmailNewPostAuthorDto newPostAuthorDto = new EmailNewPostAuthorDto(postId, flag, authorId);
				dataExchange.toNewPostAuthor().send(MessageBuilder.withPayload(newPostAuthorDto)
						.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

				EmailMatchedPostsAuthorsDto matchedPostsAuthorsDto = new EmailMatchedPostsAuthorsDto(postId, flag,
						searchingService.getAuthorsOfMatchingPosts(postId, flag));
				dataExchange.toAllMatchedPostsAuthors().send(MessageBuilder.withPayload(matchedPostsAuthorsDto)
						.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
			} else
				throw new PostNotFoundException();
		} else
			throw new EmptyDataDtoException();
	}
	
}
