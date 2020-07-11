package proPets.searching.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DataExchange {
	
	String NEW_MATCHED = "newMatched";
	String ALL_MATCHED = "allMatched";
	String INPUT = "addEditPost";
    
    @Input(INPUT)
    SubscribableChannel inboundPost();
	
	@Output(NEW_MATCHED)
	MessageChannel toAllMatchedPostsAuthors();
	
	@Output(ALL_MATCHED)
	MessageChannel toNewPostAuthor();

}
